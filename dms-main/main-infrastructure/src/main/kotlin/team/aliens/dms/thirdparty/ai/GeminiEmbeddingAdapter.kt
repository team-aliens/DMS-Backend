package team.aliens.dms.thirdparty.ai

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import team.aliens.dms.domain.chatbot.exception.ChatbotEmbeddingFailedException
import team.aliens.dms.domain.chatbot.spi.EmbeddingPort
import team.aliens.dms.thirdparty.ai.client.GeminiClient
import team.aliens.dms.thirdparty.ai.client.dto.GeminiBatchEmbedContentsRequest
import kotlin.math.sqrt

@Component
class GeminiEmbeddingAdapter(
    @Value("\${gemini.api-key}")
    private val apiKey: String,
    @Value("\${chatbot.embedding.model}")
    private val model: String,
    @Value("\${chatbot.embedding.dimension}")
    private val dimension: Int,
    private val geminiClient: GeminiClient
) : EmbeddingPort {

    private val logger = LoggerFactory.getLogger(GeminiEmbeddingAdapter::class.java)

    override fun embeddingModel(): String = "$model/$dimension"

    override fun embedAll(texts: List<String>): List<FloatArray> {
        if (texts.isEmpty()) return emptyList()

        return texts.chunked(BATCH_SIZE).flatMap { batch ->
            val response = runCatching {
                geminiClient.batchEmbedContents(
                    model = model,
                    apiKey = apiKey,
                    request = GeminiBatchEmbedContentsRequest.ofDocuments(model, dimension, batch)
                )
            }.onFailure {
                logger.error("Gemini 임베딩 요청에 실패했습니다.", it)
            }.getOrNull() ?: throw ChatbotEmbeddingFailedException

            if (response.embeddings.size != batch.size) {
                logger.error("Gemini 임베딩 개수가 요청과 다릅니다. 요청={} 응답={}", batch.size, response.embeddings.size)
                throw ChatbotEmbeddingFailedException
            }
            response.embeddings.map { normalize(it.values.toFloatArray()) }
        }
    }

   // Gemini가 반환한 임베딩 벡터의 길이를 1로 맞춰서, 코사인 유사도와 내적을 동일하게 계산할 수 있도록 하는 것.
   private fun normalize(vector: FloatArray): FloatArray {
        if (vector.size != dimension) {
            logger.error("Gemini 임베딩 차원이 설정과 다릅니다. 설정={} 응답={}", dimension, vector.size)
            throw ChatbotEmbeddingFailedException
        }
        val norm = sqrt(vector.fold(0.0) { acc, v -> acc + v * v })
        if (norm == 0.0) return vector
        return FloatArray(vector.size) { (vector[it] / norm).toFloat() }
    }

    companion object {
        // batchEmbedContents 한 번에 보낼 수 있는 요청 수 상한
        private const val BATCH_SIZE = 100
    }
}

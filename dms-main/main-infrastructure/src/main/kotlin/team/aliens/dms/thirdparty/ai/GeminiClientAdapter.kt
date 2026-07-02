package team.aliens.dms.thirdparty.ai

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import team.aliens.dms.domain.chatbot.exception.ChatbotAnswerGenerationFailedException
import team.aliens.dms.domain.chatbot.spi.ChatAiPort
import team.aliens.dms.thirdparty.ai.client.GeminiClient
import team.aliens.dms.thirdparty.ai.client.dto.GeminiGenerateContentRequest

@Component
class GeminiClientAdapter(
    @Value("\${gemini.api-key}")
    private val apiKey: String,
    @Value("\${gemini.model}")
    private val model: String,
    private val geminiClient: GeminiClient
) : ChatAiPort {

    private val logger = LoggerFactory.getLogger(GeminiClientAdapter::class.java)

    override fun generateAnswer(systemInstruction: String, question: String): String {
        return runCatching {
            geminiClient.generateContent(
                model = model,
                apiKey = apiKey,
                request = GeminiGenerateContentRequest.of(systemInstruction, question)
            ).firstText()
        }.onFailure {
            logger.error("Gemini 응답 생성에 실패했습니다.", it)
        }.getOrNull() ?: throw ChatbotAnswerGenerationFailedException
    }
}

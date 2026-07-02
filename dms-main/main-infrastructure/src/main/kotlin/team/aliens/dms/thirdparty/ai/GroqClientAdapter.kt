package team.aliens.dms.thirdparty.ai

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import team.aliens.dms.domain.chatbot.exception.ChatbotAnswerGenerationFailedException
import team.aliens.dms.domain.chatbot.spi.ChatAiPort
import team.aliens.dms.thirdparty.ai.client.GroqClient
import team.aliens.dms.thirdparty.ai.client.dto.GroqChatCompletionRequest

@Component
class GroqClientAdapter(
    @Value("\${groq.api-key}")
    private val apiKey: String,
    @Value("\${groq.model}")
    private val model: String,
    private val groqClient: GroqClient
) : ChatAiPort {

    private val logger = LoggerFactory.getLogger(GroqClientAdapter::class.java)

    override fun generateAnswer(systemInstruction: String, question: String): String {
        return runCatching {
            groqClient.chatCompletion(
                authorization = "Bearer $apiKey",
                request = GroqChatCompletionRequest.of(model, systemInstruction, question)
            ).firstText()
        }.onFailure {
            logger.error("Groq 응답 생성에 실패했습니다.", it)
        }.getOrNull() ?: throw ChatbotAnswerGenerationFailedException
    }
}

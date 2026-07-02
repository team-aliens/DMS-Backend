package team.aliens.dms.thirdparty.ai

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import team.aliens.dms.domain.chatbot.exception.ChatbotAnswerGenerationFailedException
import team.aliens.dms.domain.chatbot.spi.ChatAiPort
import team.aliens.dms.thirdparty.ai.client.GroqClient
import team.aliens.dms.thirdparty.ai.client.dto.GroqChatCompletionRequest

/**
 * Groq 기반 ChatAiPort 구현체.
 *
 * @Primary 로 지정되어 있어, ChatAiPort 주입 시 기존 GeminiClientAdapter 대신 이 구현이 사용된다.
 * Gemini 로 되돌리려면 이 @Primary 를 제거(또는 이 클래스 제거)하면 된다.
 */
@Primary
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

package team.aliens.dms.domain.chatbot.usecase

import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.chatbot.dto.ChatbotAnswerResponse
import team.aliens.dms.domain.chatbot.model.ChatbotQueryLog
import team.aliens.dms.domain.chatbot.service.ChatbotService
import java.time.LocalDateTime

@UseCase
class AskChatbotUseCase(
    private val chatbotService: ChatbotService,
    private val securityService: SecurityService
) {

    // LLM 호출이 수 초 걸리므로 그동안 DB 커넥션을 잡고 있지 않도록 트랜잭션 밖에서 실행한다
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun execute(question: String): ChatbotAnswerResponse {
        val schoolId = securityService.getCurrentSchoolId()
        val startedAt = System.currentTimeMillis()

        val result = runCatching { chatbotService.generateAnswer(schoolId, question) }

        // 실패해도 FAILED 로 기록하고, 원래 예외는 그대로 던진다
        chatbotService.saveChatbotQueryLog(
            ChatbotQueryLog.of(
                schoolId = schoolId,
                question = question,
                answer = result.getOrNull(),
                responseTimeMs = System.currentTimeMillis() - startedAt,
                createdAt = LocalDateTime.now()
            )
        )

        return ChatbotAnswerResponse(result.getOrThrow().answer)
    }
}

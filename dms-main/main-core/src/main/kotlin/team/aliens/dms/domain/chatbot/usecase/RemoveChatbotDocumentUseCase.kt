package team.aliens.dms.domain.chatbot.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.chatbot.exception.ChatbotDocumentSchoolMismatchException
import team.aliens.dms.domain.chatbot.service.ChatbotService
import java.util.UUID

@UseCase
class RemoveChatbotDocumentUseCase(
    private val chatbotService: ChatbotService,
    private val securityService: SecurityService
) {

    fun execute(documentId: UUID) {
        val document = chatbotService.getChatbotDocumentById(documentId)

        if (document.schoolId != securityService.getCurrentSchoolId()) {
            throw ChatbotDocumentSchoolMismatchException
        }

        chatbotService.deleteChatbotDocument(document)
    }
}

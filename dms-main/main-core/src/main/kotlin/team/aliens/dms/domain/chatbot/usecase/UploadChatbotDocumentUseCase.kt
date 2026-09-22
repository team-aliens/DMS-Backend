package team.aliens.dms.domain.chatbot.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.chatbot.dto.ChatbotDocumentIdResponse
import team.aliens.dms.domain.chatbot.exception.ChatbotDocumentInvalidExtensionException
import team.aliens.dms.domain.chatbot.model.ChatbotDocument
import team.aliens.dms.domain.chatbot.service.ChatbotService
import java.time.LocalDateTime

@UseCase
class UploadChatbotDocumentUseCase(
    private val chatbotService: ChatbotService,
    private val securityService: SecurityService
) {

    fun execute(fileName: String, content: String, publishedAt: LocalDateTime): ChatbotDocumentIdResponse {
        if (!ChatbotDocument.isAllowedExtension(fileName)) {
            throw ChatbotDocumentInvalidExtensionException
        }

        val document = chatbotService.ingestChatbotDocument(
            schoolId = securityService.getCurrentSchoolId(),
            fileName = fileName,
            content = content,
            publishedAt = publishedAt
        )

        return ChatbotDocumentIdResponse(document.id)
    }
}

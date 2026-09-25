package team.aliens.dms.domain.chatbot.service

import team.aliens.dms.domain.chatbot.model.ChatbotDocument
import java.util.UUID

interface GetChatbotService {

    fun generateAnswer(question: String): String

    fun getChatbotDocumentById(documentId: UUID): ChatbotDocument
}

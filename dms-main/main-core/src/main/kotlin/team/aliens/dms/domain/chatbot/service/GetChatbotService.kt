package team.aliens.dms.domain.chatbot.service

import team.aliens.dms.domain.chatbot.model.ChatbotAnswer
import team.aliens.dms.domain.chatbot.model.ChatbotDocument
import java.util.UUID

interface GetChatbotService {

    fun generateAnswer(schoolId: UUID, question: String): ChatbotAnswer

    fun getChatbotDocumentById(documentId: UUID): ChatbotDocument
}

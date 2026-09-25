package team.aliens.dms.domain.chatbot.spi

import team.aliens.dms.domain.chatbot.model.ChatbotDocument
import java.util.UUID

interface QueryChatbotDocumentPort {

    fun queryChatbotDocumentById(documentId: UUID): ChatbotDocument?

    fun queryChatbotDocumentBySchoolIdAndSourceUri(schoolId: UUID, sourceUri: String): ChatbotDocument?
}

package team.aliens.dms.domain.chatbot.spi

import team.aliens.dms.domain.chatbot.model.ChatbotDocumentChunk
import java.util.UUID

interface CommandChatbotDocumentChunkPort {

    fun saveAllChatbotDocumentChunks(chunks: List<ChatbotDocumentChunk>): List<ChatbotDocumentChunk>

    fun deleteChatbotDocumentChunksByDocumentId(documentId: UUID)
}

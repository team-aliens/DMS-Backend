package team.aliens.dms.persistence.chatbot

import org.springframework.stereotype.Component
import team.aliens.dms.domain.chatbot.model.ChatbotDocumentChunk
import team.aliens.dms.domain.chatbot.spi.ChatbotDocumentChunkPort
import team.aliens.dms.persistence.chatbot.mapper.ChatbotDocumentChunkMapper
import team.aliens.dms.persistence.chatbot.repository.ChatbotDocumentChunkJpaRepository
import java.util.UUID

@Component
class ChatbotDocumentChunkPersistenceAdapter(
    private val chatbotDocumentChunkMapper: ChatbotDocumentChunkMapper,
    private val chatbotDocumentChunkRepository: ChatbotDocumentChunkJpaRepository
) : ChatbotDocumentChunkPort {

    override fun saveAllChatbotDocumentChunks(chunks: List<ChatbotDocumentChunk>): List<ChatbotDocumentChunk> {
        val entities = chunks.map { chatbotDocumentChunkMapper.toEntity(it) }

        return chatbotDocumentChunkRepository.saveAll(entities)
            .map { chatbotDocumentChunkMapper.toDomain(it)!! }
    }

    override fun deleteChatbotDocumentChunksByDocumentId(documentId: UUID) {
        chatbotDocumentChunkRepository.deleteByDocumentId(documentId)
    }
}

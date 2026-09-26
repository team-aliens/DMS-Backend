package team.aliens.dms.persistence.chatbot

import org.springframework.stereotype.Component
import team.aliens.dms.domain.chatbot.model.ChatbotDocumentChunk
import team.aliens.dms.domain.chatbot.model.RetrievedChunk
import team.aliens.dms.domain.chatbot.spi.ChatbotDocumentChunkPort
import team.aliens.dms.persistence.chatbot.mapper.ChatbotDocumentChunkMapper
import team.aliens.dms.persistence.chatbot.repository.ChatbotDocumentChunkJpaRepository
import java.util.UUID

@Component
class ChatbotDocumentChunkPersistenceAdapter(
    private val chatbotDocumentChunkMapper: ChatbotDocumentChunkMapper,
    private val chatbotDocumentChunkRepository: ChatbotDocumentChunkJpaRepository
) : ChatbotDocumentChunkPort {

    override fun searchChatbotDocumentChunks(
        schoolId: UUID,
        queryEmbedding: FloatArray,
        limit: Int
    ): List<RetrievedChunk> {
        return chatbotDocumentChunkRepository.searchBySchoolIdOrderByCosineDistance(
            schoolId = schoolId,
            embedding = toVectorLiteral(queryEmbedding),
            limit = limit
        ).map {
            RetrievedChunk(
                chunkId = it.chunkId,
                documentTitle = it.documentTitle,
                sectionPath = it.sectionPath,
                content = it.content,
                distance = it.distance
            )
        }
    }

    override fun saveAllChatbotDocumentChunks(chunks: List<ChatbotDocumentChunk>): List<ChatbotDocumentChunk> {
        val entities = chunks.map { chatbotDocumentChunkMapper.toEntity(it) }

        return chatbotDocumentChunkRepository.saveAll(entities)
            .map { chatbotDocumentChunkMapper.toDomain(it)!! }
    }

    override fun deleteChatbotDocumentChunksByDocumentId(documentId: UUID) {
        chatbotDocumentChunkRepository.deleteByDocumentId(documentId)
    }

    // pgvector 의 텍스트 표현 "[v1,v2,...]". CAST(... AS vector) 로 넘긴다
    private fun toVectorLiteral(embedding: FloatArray) =
        embedding.joinToString(separator = ",", prefix = "[", postfix = "]")
}

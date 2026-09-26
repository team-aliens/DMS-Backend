package team.aliens.dms.persistence.chatbot

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.chatbot.model.ChatbotDocumentChunk
import team.aliens.dms.domain.chatbot.spi.ChatbotDocumentChunkPort
import team.aliens.dms.domain.chatbot.spi.vo.RetrievedChunkVO
import team.aliens.dms.persistence.chatbot.entity.QChatbotDocumentChunkJpaEntity.chatbotDocumentChunkJpaEntity
import team.aliens.dms.persistence.chatbot.entity.QChatbotDocumentJpaEntity.chatbotDocumentJpaEntity
import team.aliens.dms.persistence.chatbot.mapper.ChatbotDocumentChunkMapper
import team.aliens.dms.persistence.chatbot.repository.ChatbotDocumentChunkJpaRepository
import team.aliens.dms.persistence.chatbot.repository.vo.QQueryRetrievedChunkVO
import java.util.UUID

@Component
class ChatbotDocumentChunkPersistenceAdapter(
    private val chatbotDocumentChunkMapper: ChatbotDocumentChunkMapper,
    private val chatbotDocumentChunkRepository: ChatbotDocumentChunkJpaRepository,
    private val queryFactory: JPAQueryFactory
) : ChatbotDocumentChunkPort {

    /**
     * cosine_distance 는 hibernate-vector 가 등록한 HQL 함수로, SQL 에서는 pgvector 의 <=>(코사인 거리)가 된다.
     * 학교당 청크 수가 적어 ANN 인덱스 없이 전수 비교(정확 검색)한다.
     * 나중에 HNSW 인덱스를 추가하면 연산자와 짝이 맞는 vector_cosine_ops 로 만들어야 인덱스를 탄다.
     */
    override fun searchChatbotDocumentChunks(
        schoolId: UUID,
        queryEmbedding: FloatArray,
        limit: Int
    ): List<RetrievedChunkVO> {
        val distance = Expressions.numberTemplate(
            Double::class.javaObjectType,
            "cosine_distance({0}, {1})",
            chatbotDocumentChunkJpaEntity.embedding,
            queryEmbedding
        )

        return queryFactory
            .select(
                QQueryRetrievedChunkVO(
                    chatbotDocumentChunkJpaEntity.id,
                    chatbotDocumentJpaEntity.title,
                    chatbotDocumentChunkJpaEntity.sectionPath,
                    chatbotDocumentChunkJpaEntity.content,
                    distance
                )
            )
            .from(chatbotDocumentChunkJpaEntity)
            .join(chatbotDocumentChunkJpaEntity.document, chatbotDocumentJpaEntity)
            .where(chatbotDocumentChunkJpaEntity.school.id.eq(schoolId))
            .orderBy(distance.asc())
            .limit(limit.toLong())
            .fetch()
    }

    override fun saveAllChatbotDocumentChunks(chunks: List<ChatbotDocumentChunk>): List<ChatbotDocumentChunk> {
        val entities = chunks.map { chatbotDocumentChunkMapper.toEntity(it) }

        return chatbotDocumentChunkRepository.saveAll(entities)
            .map { chatbotDocumentChunkMapper.toDomain(it)!! }
    }

    override fun deleteChatbotDocumentChunksByDocumentId(documentId: UUID) {
        chatbotDocumentChunkRepository.deleteByDocumentId(documentId)
    }
}

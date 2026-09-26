package team.aliens.dms.persistence.chatbot.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.chatbot.entity.ChatbotDocumentChunkJpaEntity
import team.aliens.dms.persistence.chatbot.repository.vo.QueryRetrievedChunkVO
import java.util.UUID

@Repository
interface ChatbotDocumentChunkJpaRepository : CrudRepository<ChatbotDocumentChunkJpaEntity, UUID> {

    // 파생 delete 는 em.remove 로 예약만 하고, flush 때 Hibernate 가 INSERT 를 DELETE 보다 먼저 실행한다.
    // 그러면 같은 (document_id, order_index) 의 새 청크가 옛 청크보다 먼저 들어가 UNIQUE 에 걸리므로 바로 삭제한다
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from ChatbotDocumentChunkJpaEntity c where c.document.id = :documentId")
    fun deleteByDocumentId(@Param("documentId") documentId: UUID)

    // <=> 는 코사인 거리. 학교당 청크 수가 적어 ANN 인덱스 없이 전수 비교(정확 검색)한다.
    // 나중에 HNSW 인덱스를 추가하면 연산자와 짝이 맞는 vector_cosine_ops 로 만들어야 인덱스를 탄다.
    // PostgreSQL 은 따옴표 없는 별칭을 소문자로 바꾸므로 프로젝션 게터와 맞추려고 별칭에 따옴표를 붙인다
    @Query(
        value = """
            SELECT c.id AS "chunkId", d.title AS "documentTitle", c.section_path AS "sectionPath",
                   c.content AS "content", c.embedding <=> CAST(:embedding AS vector) AS "distance"
            FROM tbl_chatbot_document_chunk c
            JOIN tbl_chatbot_document d ON d.id = c.document_id
            WHERE c.school_id = :schoolId
            ORDER BY c.embedding <=> CAST(:embedding AS vector)
            LIMIT :limit
        """,
        nativeQuery = true
    )
    fun searchBySchoolIdOrderByCosineDistance(
        @Param("schoolId") schoolId: UUID,
        @Param("embedding") embedding: String,
        @Param("limit") limit: Int
    ): List<QueryRetrievedChunkVO>
}

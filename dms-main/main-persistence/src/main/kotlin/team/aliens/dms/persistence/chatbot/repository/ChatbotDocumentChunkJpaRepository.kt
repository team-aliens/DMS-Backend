package team.aliens.dms.persistence.chatbot.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.chatbot.entity.ChatbotDocumentChunkJpaEntity
import java.util.UUID

@Repository
interface ChatbotDocumentChunkJpaRepository : CrudRepository<ChatbotDocumentChunkJpaEntity, UUID> {

    // 파생 delete 는 em.remove 로 예약만 하고, flush 때 Hibernate 가 INSERT 를 DELETE 보다 먼저 실행한다.
    // 그러면 같은 (document_id, order_index) 의 새 청크가 옛 청크보다 먼저 들어가 UNIQUE 에 걸리므로 바로 삭제한다
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from ChatbotDocumentChunkJpaEntity c where c.document.id = :documentId")
    fun deleteByDocumentId(@Param("documentId") documentId: UUID)
}

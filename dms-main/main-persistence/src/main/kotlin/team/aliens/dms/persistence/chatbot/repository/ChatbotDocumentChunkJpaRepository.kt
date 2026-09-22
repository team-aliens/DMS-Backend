package team.aliens.dms.persistence.chatbot.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.chatbot.entity.ChatbotDocumentChunkJpaEntity
import java.util.UUID

@Repository
interface ChatbotDocumentChunkJpaRepository : CrudRepository<ChatbotDocumentChunkJpaEntity, UUID> {

    fun deleteByDocumentId(documentId: UUID)
}

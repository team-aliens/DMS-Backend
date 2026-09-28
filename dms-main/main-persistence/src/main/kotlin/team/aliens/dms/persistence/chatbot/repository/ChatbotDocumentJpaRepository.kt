package team.aliens.dms.persistence.chatbot.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.chatbot.entity.ChatbotDocumentJpaEntity
import java.util.UUID

@Repository
interface ChatbotDocumentJpaRepository : CrudRepository<ChatbotDocumentJpaEntity, UUID> {

    fun findBySchoolIdAndSourceUri(schoolId: UUID, sourceUri: String): ChatbotDocumentJpaEntity?
}

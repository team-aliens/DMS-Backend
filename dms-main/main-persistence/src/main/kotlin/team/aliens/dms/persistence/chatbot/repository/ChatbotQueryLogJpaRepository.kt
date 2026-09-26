package team.aliens.dms.persistence.chatbot.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.chatbot.entity.ChatbotQueryLogJpaEntity
import java.util.UUID

@Repository
interface ChatbotQueryLogJpaRepository : CrudRepository<ChatbotQueryLogJpaEntity, UUID>

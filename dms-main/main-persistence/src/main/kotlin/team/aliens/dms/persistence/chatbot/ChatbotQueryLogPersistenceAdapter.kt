package team.aliens.dms.persistence.chatbot

import org.springframework.stereotype.Component
import team.aliens.dms.domain.chatbot.model.ChatbotQueryLog
import team.aliens.dms.domain.chatbot.spi.ChatbotQueryLogPort
import team.aliens.dms.persistence.chatbot.mapper.ChatbotQueryLogMapper
import team.aliens.dms.persistence.chatbot.repository.ChatbotQueryLogJpaRepository

@Component
class ChatbotQueryLogPersistenceAdapter(
    private val chatbotQueryLogMapper: ChatbotQueryLogMapper,
    private val chatbotQueryLogRepository: ChatbotQueryLogJpaRepository
) : ChatbotQueryLogPort {

    override fun saveChatbotQueryLog(queryLog: ChatbotQueryLog): ChatbotQueryLog {
        return chatbotQueryLogMapper.toDomain(
            chatbotQueryLogRepository.save(chatbotQueryLogMapper.toEntity(queryLog))
        )!!
    }
}

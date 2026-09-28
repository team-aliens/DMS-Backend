package team.aliens.dms.persistence.chatbot

import org.slf4j.LoggerFactory
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

    private val logger = LoggerFactory.getLogger(ChatbotQueryLogPersistenceAdapter::class.java)

    // 로그 저장 실패가 답변 응답을 실패시키거나 답변 생성 예외를 덮지 않도록 여기서 기록만 하고 삼킨다
    override fun saveChatbotQueryLog(queryLog: ChatbotQueryLog) {
        runCatching {
            chatbotQueryLogRepository.save(chatbotQueryLogMapper.toEntity(queryLog))
        }.onFailure {
            logger.error("챗봇 질의 로그 저장에 실패했습니다. schoolId={} status={}", queryLog.schoolId, queryLog.status, it)
        }
    }
}

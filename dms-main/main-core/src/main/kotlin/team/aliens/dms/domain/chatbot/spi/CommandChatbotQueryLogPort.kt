package team.aliens.dms.domain.chatbot.spi

import team.aliens.dms.domain.chatbot.model.ChatbotQueryLog

interface CommandChatbotQueryLogPort {

    // 로그는 부가 기능이라 저장에 실패해도 예외를 던지지 않는다
    fun saveChatbotQueryLog(queryLog: ChatbotQueryLog)
}

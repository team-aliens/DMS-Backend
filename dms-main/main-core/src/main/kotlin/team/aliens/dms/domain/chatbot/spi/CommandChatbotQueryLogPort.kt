package team.aliens.dms.domain.chatbot.spi

import team.aliens.dms.domain.chatbot.model.ChatbotQueryLog

interface CommandChatbotQueryLogPort {

    fun saveChatbotQueryLog(queryLog: ChatbotQueryLog): ChatbotQueryLog
}

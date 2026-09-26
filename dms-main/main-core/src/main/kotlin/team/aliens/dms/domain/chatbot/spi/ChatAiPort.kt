package team.aliens.dms.domain.chatbot.spi

import team.aliens.dms.domain.chatbot.model.ChatAiAnswer

interface ChatAiPort {

    fun generateAnswer(systemInstruction: String, question: String): ChatAiAnswer
}

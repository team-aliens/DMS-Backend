package team.aliens.dms.domain.chatbot.spi

import team.aliens.dms.domain.chatbot.spi.vo.ChatAiAnswerVO

interface ChatAiPort {

    fun generateAnswer(systemInstruction: String, question: String): ChatAiAnswerVO
}

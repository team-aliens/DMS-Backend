package team.aliens.dms.domain.chatbot.spi.vo

import team.aliens.dms.domain.chatbot.model.TokenUsage

data class ChatAiAnswerVO(
    val text: String,
    val usage: TokenUsage
)

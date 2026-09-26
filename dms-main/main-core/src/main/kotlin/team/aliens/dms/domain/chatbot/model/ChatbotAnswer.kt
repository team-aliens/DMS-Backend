package team.aliens.dms.domain.chatbot.model

import java.util.UUID

data class ChatbotAnswer(
    val answer: String,
    val mode: ChatbotAnswerMode,
    val status: ChatbotQueryStatus,
    val retrievedChunkIds: List<UUID>,
    val usage: TokenUsage
)

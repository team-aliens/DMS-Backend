package team.aliens.dms.domain.chatbot.stub

import team.aliens.dms.domain.chatbot.model.ChatbotAnswer
import team.aliens.dms.domain.chatbot.model.ChatbotQueryStatus
import team.aliens.dms.domain.chatbot.model.TokenUsage
import java.util.UUID

internal fun createChatbotAnswerStub(
    answer: String = "평일 통금 시간은 오후 10시입니다.",
    status: ChatbotQueryStatus = ChatbotQueryStatus.ANSWERED,
    retrievedChunkIds: List<UUID> = listOf(UUID.randomUUID()),
    usage: TokenUsage = TokenUsage(promptTokens = 100, candidatesTokens = 20, totalTokens = 120)
) = ChatbotAnswer(
    answer = answer,
    status = status,
    retrievedChunkIds = retrievedChunkIds,
    usage = usage
)

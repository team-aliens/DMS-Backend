package team.aliens.dms.domain.chatbot.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class ChatbotQueryLog(

    val id: UUID = UUID(0, 0),

    val question: String,

    val answerMode: ChatbotAnswerMode,

    val status: ChatbotQueryStatus,

    // 실패하면 null
    val answer: String?,

    val retrievedChunkIds: List<UUID>,

    val usage: TokenUsage,

    val responseTimeMs: Long,

    val createdAt: LocalDateTime,

    override val schoolId: UUID

) : SchoolIdDomain {

    companion object {
        // answer 가 null 이면 답변 생성이 실패한 것으로 기록한다
        fun of(
            schoolId: UUID,
            question: String,
            mode: ChatbotAnswerMode,
            answer: ChatbotAnswer?,
            responseTimeMs: Long,
            createdAt: LocalDateTime
        ) = ChatbotQueryLog(
            question = question,
            answerMode = mode,
            status = answer?.status ?: ChatbotQueryStatus.FAILED,
            answer = answer?.answer,
            retrievedChunkIds = answer?.retrievedChunkIds.orEmpty(),
            usage = answer?.usage ?: TokenUsage.EMPTY,
            responseTimeMs = responseTimeMs,
            createdAt = createdAt,
            schoolId = schoolId
        )
    }
}

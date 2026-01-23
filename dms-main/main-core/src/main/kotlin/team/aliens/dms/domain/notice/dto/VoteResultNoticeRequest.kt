package team.aliens.dms.domain.notice.dto

import java.time.LocalDateTime
import java.util.UUID

data class VoteResultNoticeRequest(
    val votingTopicId: UUID,
    val reservedTime: LocalDateTime,
    val isReNotice: Boolean,
    val managerId: UUID,
    val schoolId: UUID,
)

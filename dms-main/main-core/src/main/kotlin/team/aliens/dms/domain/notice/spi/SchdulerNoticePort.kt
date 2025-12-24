package team.aliens.dms.domain.notice.spi

import java.time.LocalDateTime
import java.util.UUID

interface SchdulerNoticePort {

    fun scheduleVoteResultNotice(votingTopicId: UUID, startTime: LocalDateTime, isReNotice: Boolean)

    fun cancelVoteResultNotice(votingTopicId: UUID)
}

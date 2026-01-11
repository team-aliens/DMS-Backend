package team.aliens.dms.domain.notice.service

import java.time.LocalDateTime
import java.util.UUID

interface SchdulerNoticeService {

    fun scheduleVoteResultNotice(votingTopicId: UUID, startTime: LocalDateTime, isReNotice: Boolean, managerId: UUID, schoolId: UUID)

    fun cancelVoteResultNotice(votingTopicId: UUID)
}

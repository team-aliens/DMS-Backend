package team.aliens.dms.domain.notice.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.notice.spi.SchdulerNoticePort
import java.time.LocalDateTime
import java.util.UUID

@Service
class SchdulerNoticeServiceImpl(
    private val schdulerNoticePort: SchdulerNoticePort
) : SchdulerNoticeService {
    override fun scheduleVoteResultNotice(
        votingTopicId: UUID,
        startTime: LocalDateTime,
        isReNotice: Boolean
    ) {
        schdulerNoticePort.scheduleVoteResultNotice(votingTopicId, startTime, isReNotice)
    }

    override fun cancelVoteResultNotice(votingTopicId: UUID) {
        schdulerNoticePort.cancelVoteResultNotice(votingTopicId)
    }
}

package team.aliens.dms.domain.notice.service

import team.aliens.dms.domain.notice.model.Notice
import java.time.LocalDateTime
import java.util.UUID

interface CommandNoticeService {

    fun saveNotice(notice: Notice): Notice

    fun deleteNotice(notice: Notice)

    fun scheduleVoteResultNotice(votingTopicId: UUID, reservedTime: LocalDateTime, isReNotice: Boolean)
}

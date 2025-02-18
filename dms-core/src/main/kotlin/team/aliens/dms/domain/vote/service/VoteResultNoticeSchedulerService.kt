package team.aliens.dms.domain.vote.service

import java.time.LocalDateTime
import java.util.*

interface VoteResultNoticeSchedulerService {

    fun scheduleVoteResultNotice(savedVotingTopicId: UUID, reservedTime: LocalDateTime)
}

package team.aliens.dms.domain.vote.service

import team.aliens.dms.domain.vote.dto.VoteResultNoticeInfo

interface VoteResultNoticeSchedulerService {

    fun scheduleVoteResultNotice(voteResultNoticeInfo: VoteResultNoticeInfo)
}

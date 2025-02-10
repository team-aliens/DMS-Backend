package team.aliens.dms.domain.vote.service

import java.util.*

interface ValidVotePeriodService {
    fun excute(voteTopicId: UUID)
}

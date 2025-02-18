package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.exception.NotVotingPeriodException
import java.time.LocalDateTime
import java.util.UUID

@Service
class ValidVotePeriodServiceImpl(
    val getVotingTopicService: GetVotingTopicService
) : ValidVotePeriodService {

    override fun execute(voteTopicId: UUID) {
        val votingTopic = getVotingTopicService.getVotingTopicById(voteTopicId)
        val startTime = votingTopic.startTime
        val endTime = votingTopic.endTime
        val currentTime = LocalDateTime.now()

        if (!(currentTime.isAfter(startTime) && currentTime.isBefore(endTime)))
            throw NotVotingPeriodException
    }
}

package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.spi.QueryVotingTopicPort
import java.time.LocalDateTime
import java.util.UUID

@Service
class ValidVotePeriodServiceImpl(
    val queryPort: QueryVotingTopicPort
): ValidVotePeriodService {

    override fun excute(voteTopicId : UUID):Boolean{
        val startTime = queryPort.findStartTimeById(voteTopicId)
        val endTime = queryPort.findEndTimeById(voteTopicId)
        val currentTime = LocalDateTime.now()

        return currentTime.isAfter(startTime) && currentTime.isBefore(endTime)
    }
}
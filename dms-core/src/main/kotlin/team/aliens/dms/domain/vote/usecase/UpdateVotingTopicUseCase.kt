package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.common.spi.TaskSchedulerPort
import team.aliens.dms.domain.vote.dto.VoteResultNoticeInfo
import team.aliens.dms.domain.vote.dto.request.UpdateVotingTopicRequest
import team.aliens.dms.domain.vote.exception.NotValidPeriodException
import team.aliens.dms.domain.vote.exception.VotingAlreadyEndedException
import team.aliens.dms.domain.vote.service.CommendVotingTopicService
import team.aliens.dms.domain.vote.service.GetVotingTopicService
import team.aliens.dms.domain.vote.service.VoteResultNoticeSchedulerService
import java.time.LocalDateTime

@UseCase
class UpdateVotingTopicUseCase(
    private val taskSchedulerPort: TaskSchedulerPort,
    private val commandVotingTopicService: CommendVotingTopicService,
    private val getVotingTopicService: GetVotingTopicService,
    private val voteResultNoticeSchedulerService: VoteResultNoticeSchedulerService,
    private val securityPort: SecurityPort
) {

    fun execute(request: UpdateVotingTopicRequest) {
        if (request.startTime.isAfter(request.endTime) || request.endTime.isBefore(LocalDateTime.now())) {
            throw NotValidPeriodException
        }

        val votingTopic = getVotingTopicService.getVotingTopicById(request.id)
        if (votingTopic.endTime.isBefore(LocalDateTime.now())) {
            throw VotingAlreadyEndedException
        }

        val managerId = securityPort.getCurrentUserId()
        val schoolId = securityPort.getCurrentUserSchoolId()

        val savedVotingTopicId = commandVotingTopicService.saveVotingTopic(
            votingTopic.copy(
                topicName = request.topicName,
                voteType = request.voteType,
                description = request.description,
                startTime = request.startTime,
                endTime = request.endTime
            )
        )

        taskSchedulerPort.cancelTask(request.id)

        val voteResultNoticeInfo = VoteResultNoticeInfo(
            savedVotingTopicId,
            request.endTime,
            managerId,
            "임시",
            "임시",
            schoolId
        )

        voteResultNoticeSchedulerService.scheduleVoteResultNotice(voteResultNoticeInfo)
    }
}

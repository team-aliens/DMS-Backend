package team.aliens.dms.domain.vote.usecase


import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.vote.dto.CreateVoteTopicRequest
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.service.CommendVotingTopicService
import team.aliens.dms.domain.vote.service.ScheduleCreateVoteResultNoticeService

@UseCase
class CreateVoteTopicUseCase(
    private val commendVotingTopicService: CommendVotingTopicService,
    private val scheduleCreateVoteResultNoticeService: ScheduleCreateVoteResultNoticeService,
    private val securityPort: SecurityPort
) {

    fun execute(request: CreateVoteTopicRequest){

        val userId = securityPort.getCurrentUserId()
        val schoolId = securityPort.getCurrentUserSchoolId()
        commendVotingTopicService.saveVoteTopic(
            VotingTopic(
                managerId = userId,
                topicName = request.topicName,
                voteDescription = request.voteDescription,
                startTime = request.startTime,
                endTime = request.endTime,
                voteType = request.voteType,
            )
        )

        scheduleCreateVoteResultNoticeService.exctue(
            request.endTime,
            userId,
            schoolId ,
            "ex)모범학생 투표 결과",
            "쿼리 메서드로 이렇게 들어감 ex)1 학년 : 홍길동,임꺽정,유씨, 2학년 ...."
        )

    }
}

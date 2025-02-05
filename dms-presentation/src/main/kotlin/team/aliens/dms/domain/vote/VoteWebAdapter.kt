package team.aliens.dms.domain.vote

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.vote.dto.CreateVoteTopicRequest
import team.aliens.dms.domain.vote.dto.request.CreateVoteTopicWebRequest
import team.aliens.dms.domain.vote.usecase.CreateVoteTopicUseCase

@RestController
@RequestMapping("/vote")
class VoteWebAdapter(
    private val createVoteTopicUseCase: CreateVoteTopicUseCase
    ) {
    @PostMapping("/voting-topic")
    fun saveVoteTopic(@RequestBody @Valid request: CreateVoteTopicWebRequest){
        createVoteTopicUseCase.execute(
            CreateVoteTopicRequest(
                topicName = request.topicName!!,
                voteDescription = request.voteDescription!!,
                startTime = request.startTime!!,
                endTime = request.endTime!!,
                voteType = request.voteType!!
            )
        )
    }

    @GetMapping
    fun healthCheck() = "OK1"
}
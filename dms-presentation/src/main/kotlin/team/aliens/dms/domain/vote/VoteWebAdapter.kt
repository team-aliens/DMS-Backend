package team.aliens.dms.domain.vote

import jakarta.validation.Valid
import org.jetbrains.annotations.NotNull
import org.springframework.web.bind.annotation.*
import team.aliens.dms.domain.vote.dto.CreateVoteTopicRequest
import team.aliens.dms.domain.vote.dto.request.CreateVoteTopicWebRequest
import team.aliens.dms.domain.vote.usecase.CreateVoteTopicUseCase
import team.aliens.dms.domain.vote.usecase.DeleteVoteTopicUseCase
import java.util.UUID

@RestController
@RequestMapping("/vote")
class VoteWebAdapter(
    private val createVoteTopicUseCase: CreateVoteTopicUseCase,
    private val deleteVoteTopicUseCase: DeleteVoteTopicUseCase
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

    @DeleteMapping("/{votingTopicId}")
    fun deleteVoteTopic(@PathVariable("votingTopicId") @NotNull votingTopicId: UUID){
        deleteVoteTopicUseCase.excute(votingTopicId)
    }


    @GetMapping
    fun healthCheck() = "OK1"
}
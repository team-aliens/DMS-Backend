package team.aliens.dms.domain.vote

import jakarta.validation.Valid
import org.jetbrains.annotations.NotNull
import org.springframework.web.bind.annotation.*
import team.aliens.dms.domain.vote.dto.request.CreateVoteTopicRequest
import team.aliens.dms.domain.vote.dto.request.CreateVotingTopicWebRequest
import team.aliens.dms.domain.vote.usecase.CreateVotingTopicUseCase
import team.aliens.dms.domain.vote.usecase.DeleteVotingTopicUseCase
import team.aliens.dms.domain.vote.usecase.QueryAllVotingTopicUseCase
import team.aliens.dms.domain.vote.usecase.QueryVotingTopicUseCase
import java.util.UUID

@RestController
@RequestMapping("/vote")
class VoteWebAdapter(
    private val createVotingTopicUseCase: CreateVotingTopicUseCase,
    private val deleteVotingTopicUseCase: DeleteVotingTopicUseCase,
    private val queryVotingTopicUseCase: QueryVotingTopicUseCase,
    private val queryAllVotingTopicUseCase: QueryAllVotingTopicUseCase,
) {
    @PostMapping("/voting-topic")
    fun saveVotingTopic(@RequestBody @Valid request: CreateVotingTopicWebRequest){
        createVotingTopicUseCase.execute(
            CreateVoteTopicRequest(
                topicName = request.topicName!!,
                description = request.description!!,
                startTime = request.startTime!!,
                endTime = request.endTime!!,
                voteType = request.voteType!!
            )
        )
    }

    @DeleteMapping("/{votingTopicId}")
    fun deleteVotingTopic(@PathVariable("votingTopicId") @NotNull votingTopicId: UUID){
        deleteVotingTopicUseCase.execute(votingTopicId)
    }

    @GetMapping("/{votingTopicId}")
    fun getVotingTopic(@PathVariable("votingTopicId") @NotNull votingTopicId: UUID){
        queryVotingTopicUseCase.execute(votingTopicId)
    }


    @GetMapping("")
    fun getAllVotingTopic(){
        queryAllVotingTopicUseCase.execute()
    }

    @GetMapping("/test")
    fun healthCheck() = "OK1"
}
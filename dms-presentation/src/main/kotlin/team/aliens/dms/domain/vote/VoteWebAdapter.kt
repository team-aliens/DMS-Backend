package team.aliens.dms.domain.vote

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import team.aliens.dms.domain.vote.dto.request.CreateVotingOptionRequest
import team.aliens.dms.domain.vote.dto.response.VotingOptionsResponse
import team.aliens.dms.domain.vote.usecase.*
import java.util.UUID

@RestController
@RequestMapping("/vote")
class VoteWebAdapter(
        private val createVoteUseCase: CreateVoteUseCase,
        private val createVotingOptionUseCase: CreateVotingOptionUseCase,
        private val queryVotesUseCase: QueryVotesUseCase,
        private val queryVotingOptionsUseCase: QueryVotingOptionsUseCase,
        private val removeVoteUseCase: RemoveVoteUseCase,
        private val removeVotingOptionUseCase: RemoveVotingOptionUseCase
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/option")
    fun createVotingOption(@RequestBody request: CreateVotingOptionRequest) {
        createVotingOptionUseCase.execute(request)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/option/{votingTopicId}")
    fun getVotingOptions(@PathVariable votingTopicId: UUID): List<VotingOptionsResponse> {
        return queryVotingOptionsUseCase.execute(votingTopicId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/option/{votingOptionId}")
    fun removeVotingOption(@PathVariable votingOptionId: UUID) {
        removeVotingOptionUseCase.execute(votingOptionId)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/student/{votingTopicId}")
    fun createVote(@PathVariable votingTopicId: UUID,
                   @RequestParam(name="selected_id") selectedId: UUID) {
        createVoteUseCase.execute(selectedId, votingTopicId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/student/{votingTopicId}")
    fun removeVote(@PathVariable votingTopicId: UUID) {
        removeVoteUseCase.execute(votingTopicId)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/result/{votingTopicId}")
    fun getVoteResults(@PathVariable votingTopicId: UUID): Any {
        return queryVotesUseCase.execute(votingTopicId)
    }
}
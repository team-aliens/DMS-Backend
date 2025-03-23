package team.aliens.dms.domain.vote

import jakarta.validation.Valid
import org.jetbrains.annotations.NotNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.vote.dto.reponse.ExcludedStudentsResponses
import team.aliens.dms.domain.vote.dto.reponse.VotingTopicsResponse
import team.aliens.dms.domain.vote.dto.request.CreateVoteTopicRequest
import team.aliens.dms.domain.vote.dto.request.CreateVotingOptionRequest
import team.aliens.dms.domain.vote.dto.request.CreateVotingOptionWebRequest
import team.aliens.dms.domain.vote.dto.request.CreateVotingTopicWebRequest
import team.aliens.dms.domain.vote.dto.request.UpdateVotingTopicRequest
import team.aliens.dms.domain.vote.dto.request.UpdateVotingTopicWebRequest
import team.aliens.dms.domain.vote.dto.response.VotesResponse
import team.aliens.dms.domain.vote.dto.response.VotingOptionsResponse
import team.aliens.dms.domain.vote.usecase.CreateVoteUseCase
import team.aliens.dms.domain.vote.usecase.CreateVotingOptionUseCase
import team.aliens.dms.domain.vote.usecase.CreateVotingTopicUseCase
import team.aliens.dms.domain.vote.usecase.DeleteExcludedStudentUseCase
import team.aliens.dms.domain.vote.usecase.DeleteVoteUseCase
import team.aliens.dms.domain.vote.usecase.DeleteVotingTopicUseCase
import team.aliens.dms.domain.vote.usecase.QueryAllExcludedStudentUseCase
import team.aliens.dms.domain.vote.usecase.QueryAllVotingTopicUseCase
import team.aliens.dms.domain.vote.usecase.QueryVotesUseCase
import team.aliens.dms.domain.vote.usecase.QueryVotingOptionsUseCase
import team.aliens.dms.domain.vote.usecase.UpdateVotingTopicUseCase
import java.util.UUID

@RestController
@RequestMapping("/votes")
class VoteWebAdapter(
    private val createVotingTopicUseCase: CreateVotingTopicUseCase,
    private val deleteVotingTopicUseCase: DeleteVotingTopicUseCase,
    private val queryAllVotingTopicUseCase: QueryAllVotingTopicUseCase,
    private val updateVotingTopicUseCase: UpdateVotingTopicUseCase,
    private val queryAllExcludedStudentUseCase: QueryAllExcludedStudentUseCase,
    private val createVoteUseCase: CreateVoteUseCase,
    private val createVotingOptionUseCase: CreateVotingOptionUseCase,
    private val queryVotesUseCase: QueryVotesUseCase,
    private val queryVotingOptionsUseCase: QueryVotingOptionsUseCase,
    private val deleteVoteUseCase: DeleteVoteUseCase,
    private val removeVotingOptionsUseCase: QueryVotingOptionsUseCase,
    private val deleteExcludedStudentUseCase: DeleteExcludedStudentUseCase
) {

    @PostMapping
    fun saveVotingTopic(@RequestBody @Valid request: CreateVotingTopicWebRequest) {
        createVotingTopicUseCase.execute(
            CreateVoteTopicRequest(
                topicName = request.topicName,
                description = request.description,
                startTime = request.startTime,
                endTime = request.endTime,
                voteType = request.voteType
            )
        )
    }

    @PatchMapping("/{voting-topic-id}")
    fun updateVotingTopic(
        @PathVariable("voting-topic-id") @NotNull votingTopicId: UUID,
        @RequestBody @Valid request: UpdateVotingTopicWebRequest
    ) {
        updateVotingTopicUseCase.execute(
            UpdateVotingTopicRequest(
                id = votingTopicId,
                topicName = request.topicName,
                description = request.description,
                startTime = request.startTime,
                endTime = request.endTime,
                voteType = request.voteType
            )
        )
    }

    @DeleteMapping("/{voting-topic-id}")
    fun deleteVotingTopic(@PathVariable("voting-topic-id") @NotNull votingTopicId: UUID) {
        deleteVotingTopicUseCase.execute(votingTopicId)
    }

    @GetMapping
    fun getAllVotingTopic(): VotingTopicsResponse {
        return queryAllVotingTopicUseCase.execute()
    }

    @GetMapping("/excluded-student")
    fun getAllExcludedStudent(): ExcludedStudentsResponses {
        return queryAllExcludedStudentUseCase.execute()
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/option")
    fun createVotingOption(@RequestBody request: CreateVotingOptionWebRequest) {
        createVotingOptionUseCase.execute(
            CreateVotingOptionRequest.of(
                request.votingTopicId,
                request.optionName
            )
        )
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/option/{voting-topic-id}")
    fun getVotingOptions(@PathVariable("voting-topic-id") votingTopicId: UUID): VotingOptionsResponse {
        return queryVotingOptionsUseCase.execute(votingTopicId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/option/{voting-option-id}")
    fun removeVotingOption(@PathVariable("voting-option-id") votingOptionId: UUID) {
        removeVotingOptionsUseCase.execute(votingOptionId)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/student/{voting-topic-id}")
    fun createVote(
        @PathVariable("voting-topic-id") votingTopicId: UUID,
        @RequestParam(name = "selected-id") selectedId: UUID
    ) {
        createVoteUseCase.execute(selectedId, votingTopicId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/student/{voting-topic-id}")
    fun removeVote(@PathVariable("voting-topic-id") votingTopicId: UUID) {
        deleteVoteUseCase.execute(votingTopicId)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/result/{voting-topic-id}")
    fun getVoteResults(@PathVariable("voting-topic-id") votingTopicId: UUID): VotesResponse {
        return queryVotesUseCase.execute(votingTopicId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/excluded-student/{excluded-student_id}")
    fun deleteExcludedStudent(@PathVariable("excluded-student_id") excludedStudentId: UUID) {
        deleteExcludedStudentUseCase.execute(excludedStudentId)
    }
}

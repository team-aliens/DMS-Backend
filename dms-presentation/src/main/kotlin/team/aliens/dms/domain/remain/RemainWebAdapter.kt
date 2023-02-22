package team.aliens.dms.domain.remain

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.remain.dto.QueryRemainAvailableTimeResponse
import team.aliens.dms.domain.remain.dto.UpdateRemainAvailableTimeRequest
import team.aliens.dms.domain.remain.dto.request.CreateRemainOptionWebRequest
import team.aliens.dms.domain.remain.dto.request.UpdateRemainAvailableTimeWebRequest
import team.aliens.dms.domain.remain.dto.request.UpdateRemainOptionWebRequest
import team.aliens.dms.domain.remain.dto.response.CreateRemainOptionResponse
import team.aliens.dms.domain.remain.usecase.CreateRemainOptionUseCase
import team.aliens.dms.domain.remain.usecase.QueryRemainAvailableTimeUseCase
import team.aliens.dms.domain.remain.usecase.UpdateRemainAvailableTimeUseCase
import team.aliens.dms.domain.remain.usecase.UpdateRemainOptionUseCase
import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Validated
@RequestMapping("/remains")
@RestController
class RemainWebAdapter(
    private val createRemainOptionUseCase: CreateRemainOptionUseCase,
    private val updateRemainOptionUseCase: UpdateRemainOptionUseCase,
    private val queryRemainAvailableTimeUseCase: QueryRemainAvailableTimeUseCase,
    private val updateRemainAvailableTimeUseCase: UpdateRemainAvailableTimeUseCase
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/options")
    fun createRemainOption(@RequestBody @Valid request: CreateRemainOptionWebRequest): CreateRemainOptionResponse {
        val remainOptionId = createRemainOptionUseCase.execute(
            title = request.title!!,
            description = request.description!!
        )
        return CreateRemainOptionResponse(remainOptionId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/options/{remain-option-id}")
    fun updateRemainOption(
        @PathVariable("remain-option-id") @NotNull remainOptionId: UUID?,
        @RequestBody @Valid request: UpdateRemainOptionWebRequest
    ) {
        updateRemainOptionUseCase.execute(
            remainOptionId = remainOptionId!!,
            title = request.title!!,
            description = request.description!!
        )
    }

    @GetMapping("/available-time")
    fun getRemainAvailableTime(): QueryRemainAvailableTimeResponse {
        return queryRemainAvailableTimeUseCase.execute()
    }

    @PutMapping("/available-time")
    fun updateRemainAvailableTime(@RequestBody @Valid request: UpdateRemainAvailableTimeWebRequest) {
        updateRemainAvailableTimeUseCase.execute(
            UpdateRemainAvailableTimeRequest(
                startDayOfWeek = request.startDayOfWeek!!,
                startTime = request.startTime!!,
                endDayOfWeek = request.endDayOfWeek!!,
                endTime = request.endTime!!
            )
        )   
    }    
}
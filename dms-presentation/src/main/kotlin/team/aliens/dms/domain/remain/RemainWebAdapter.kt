package team.aliens.dms.domain.remain

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.remain.dto.UpdateRemainAvailableTimeRequest
import team.aliens.dms.domain.remain.dto.request.CreateRemainOptionWebRequest
import team.aliens.dms.domain.remain.dto.request.UpdateRemainAvailableTimeWebRequest
import team.aliens.dms.domain.remain.dto.response.CreateRemainOptionResponse
import team.aliens.dms.domain.remain.usecase.CreateRemainOptionUseCase
import team.aliens.dms.domain.remain.usecase.UpdateRemainAvailableTimeUseCase
import javax.validation.Valid

@Validated
@RequestMapping("/remains")
@RestController
class RemainWebAdapter(
    private val createRemainOptionUseCase: CreateRemainOptionUseCase,
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
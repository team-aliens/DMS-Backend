package team.aliens.dms.domain.studyroom

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.studyroom.dto.QuerySeatTypesResponse
import team.aliens.dms.domain.studyroom.dto.QueryAvailableTimeResponse
import team.aliens.dms.domain.studyroom.dto.UpdateAvailableTimeWebRequest
import team.aliens.dms.domain.studyroom.usecase.QuerySeatTypesUseCase
import javax.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import team.aliens.dms.domain.studyroom.dto.CreateSeatTypeWebRequest
import team.aliens.dms.domain.studyroom.usecase.CreateSeatTypeUseCase
import team.aliens.dms.domain.studyroom.usecase.QueryAvailableTimeUseCase
import team.aliens.dms.domain.studyroom.usecase.UpdateAvailableTimeUseCase

@RequestMapping("/study-rooms")
@RestController
class StudyRoomWebAdapter(
    private val queryAvailableTimeUseCase: QueryAvailableTimeUseCase,
    private val updateAvailableTimeUseCase: UpdateAvailableTimeUseCase,
    private val querySeatTypesUseCase: QuerySeatTypesUseCase,
    private val createSeatTypeUseCase: CreateSeatTypeUseCase
) {

    @GetMapping("/available-time")
    fun getAvailableTime(): QueryAvailableTimeResponse {
        return queryAvailableTimeUseCase.execute()
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/available-time")
    fun updateAvailableTime(@RequestBody @Valid request: UpdateAvailableTimeWebRequest) {
        updateAvailableTimeUseCase.execute(
            startAt = request.startAt!!,
            endAt = request.endAt!!
        )
    }

    @GetMapping("/types")
    fun getSeatTypes(): QuerySeatTypesResponse {
        return querySeatTypesUseCase.execute()
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/types")
    fun createSeatType(@RequestBody @Valid request: CreateSeatTypeWebRequest) {
        return createSeatTypeUseCase.execute(
            name = request.name!!,
            color = request.color!!
        )
    }
}
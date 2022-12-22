package team.aliens.dms.domain.studyroom

import java.util.UUID
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
import javax.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import team.aliens.dms.domain.studyroom.dto.CreateSeatTypeWebRequest
import team.aliens.dms.domain.studyroom.dto.QueryStudyRoomStudentResponse
import team.aliens.dms.domain.studyroom.usecase.ApplySeatUseCase
import team.aliens.dms.domain.studyroom.usecase.CreateSeatTypeUseCase
import team.aliens.dms.domain.studyroom.usecase.UnApplySeatUseCase
import team.aliens.dms.domain.studyroom.usecase.QueryAvailableTimeUseCase
import team.aliens.dms.domain.studyroom.usecase.QueryStudyRoomStudentUseCase
import team.aliens.dms.domain.studyroom.usecase.UpdateAvailableTimeUseCase

@Validated
@RequestMapping("/study-rooms")
@RestController
class StudyRoomWebAdapter(
    private val queryAvailableTimeUseCase: QueryAvailableTimeUseCase,
    private val updateAvailableTimeUseCase: UpdateAvailableTimeUseCase,
    private val querySeatTypesUseCase: QuerySeatTypesUseCase,
    private val createSeatTypeUseCase: CreateSeatTypeUseCase,
    private val applySeatUseCase: ApplySeatUseCase,
    private val unApplySeatUseCase: UnApplySeatUseCase,
    private val queryStudyRoomStudentUseCase: QueryStudyRoomStudentUseCase
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/seats/{seat-id}")
    fun applySeat(@PathVariable("seat-id") @NotNull seatId: UUID?) {
        return applySeatUseCase.execute(seatId!!)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/seats")
    fun unApplySeat() {
        unApplySeatUseCase.execute()
    }

    @GetMapping("/{study-room-id}/students")
    fun getStudyRoomStudent(@PathVariable("study-room-id") @NotNull studyRoomId: UUID): QueryStudyRoomStudentResponse {
        return queryStudyRoomStudentUseCase.execute(studyRoomId)
    }
}
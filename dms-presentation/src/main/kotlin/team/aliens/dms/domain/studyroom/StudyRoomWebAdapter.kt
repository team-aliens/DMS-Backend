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
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import team.aliens.dms.domain.studyroom.dto.CreateSeatTypeWebRequest
import team.aliens.dms.domain.studyroom.dto.CreateStudyRoomRequest
import team.aliens.dms.domain.studyroom.dto.CreateStudyRoomResponse
import team.aliens.dms.domain.studyroom.dto.CreateStudyRoomWebRequest
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomResponse
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse
import team.aliens.dms.domain.studyroom.dto.UpdateStudyRoomRequest
import team.aliens.dms.domain.studyroom.dto.UpdateStudyRoomWebRequest
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomsResponse
import team.aliens.dms.domain.studyroom.usecase.ApplySeatUseCase
import team.aliens.dms.domain.studyroom.usecase.CreateSeatTypeUseCase
import team.aliens.dms.domain.studyroom.usecase.CreateStudyRoomUseCase
import team.aliens.dms.domain.studyroom.usecase.ManagerQueryStudyRoomUseCase
import team.aliens.dms.domain.studyroom.usecase.UnApplySeatUseCase
import team.aliens.dms.domain.studyroom.usecase.QueryAvailableTimeUseCase
import team.aliens.dms.domain.studyroom.usecase.RemoveStudyRoomUseCase
import team.aliens.dms.domain.studyroom.usecase.StudentQueryStudyRoomUseCase
import team.aliens.dms.domain.studyroom.usecase.StudentQueryStudyRoomsUseCase
import team.aliens.dms.domain.studyroom.usecase.UpdateAvailableTimeUseCase
import team.aliens.dms.domain.studyroom.usecase.UpdateStudyRoomUseCase

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
    private val createStudyRoomUseCase: CreateStudyRoomUseCase,
    private val updateStudyRoomUseCase: UpdateStudyRoomUseCase,
    private val studentQueryStudyRoomUseCase: StudentQueryStudyRoomUseCase,
    private val removeStudyRoomUseCase: RemoveStudyRoomUseCase,
    private val managerQueryStudyRoomUseCase: ManagerQueryStudyRoomUseCase,
    private val studentQueryStudyRoomsUseCase: StudentQueryStudyRoomsUseCase
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createStudyRoom(@RequestBody @Valid request: CreateStudyRoomWebRequest): CreateStudyRoomResponse {
        val studyRoomId = createStudyRoomUseCase.execute(
            request.run {
                CreateStudyRoomRequest(
                    floor = floor!!,
                    name = name!!,
                    totalWidthSize = totalWidthSize!!,
                    totalHeightSize = totalHeightSize!!,
                    eastDescription = eastDescription!!,
                    westDescription = westDescription!!,
                    southDescription = southDescription!!,
                    northDescription = northDescription!!,
                    availableSex = availableSex!!,
                    availableGrade = availableGrade!!,
                    seats = seats.map {
                        CreateStudyRoomRequest.SeatRequest(
                            widthLocation = it.widthLocation!!,
                            heightLocation = it.heightLocation!!,
                            number = it.number,
                            typeId = it.typeId,
                            status = it.status!!.name
                        )
                    }
                )
            }
        )

        return CreateStudyRoomResponse(studyRoomId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{study-room-id}")
    fun updateStudyRoom(
        @PathVariable("study-room-id") @NotNull studyRoomId: UUID?,
        @RequestBody @Valid request: UpdateStudyRoomWebRequest
    ) {
        updateStudyRoomUseCase.execute(
            studyRoomId!!,
            request.run {
                UpdateStudyRoomRequest(
                    floor = floor!!,
                    name = name!!,
                    totalWidthSize = totalWidthSize!!,
                    totalHeightSize = totalHeightSize!!,
                    eastDescription = eastDescription!!,
                    westDescription = westDescription!!,
                    southDescription = southDescription!!,
                    northDescription = northDescription!!,
                    availableSex = availableSex!!,
                    availableGrade = availableGrade!!,
                    seats = seats.map {
                        UpdateStudyRoomRequest.SeatRequest(
                            widthLocation = it.widthLocation!!,
                            heightLocation = it.heightLocation!!,
                            number = it.number,
                            typeId = it.typeId,
                            status = it.status!!.name
                        )
                    }
                )
            }
        )
    }

    @GetMapping("/{study-room-id}/students")
    fun studentGetStudyRoom(@PathVariable("study-room-id") @NotNull studyRoomId: UUID?): StudentQueryStudyRoomResponse {
        return studentQueryStudyRoomUseCase.execute(studyRoomId!!)
    }

    @GetMapping("/{study-room-id}/managers")
    fun managerGetStudyRoom(@PathVariable("study-room-id") @NotNull studyRoomId: UUID?): ManagerQueryStudyRoomResponse {
        return managerQueryStudyRoomUseCase.execute(studyRoomId!!)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{study-room-id}")
    fun removeStudyRoom(@PathVariable("study-room-id") @NotNull studyRoomId: UUID) {
        removeStudyRoomUseCase.execute(studyRoomId)
    }

    @GetMapping("/list/students")
    fun studentGetStudyRooms(): StudentQueryStudyRoomsResponse {
        return studentQueryStudyRoomsUseCase.execute()
    }
}
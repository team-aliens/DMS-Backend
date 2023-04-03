package team.aliens.dms.domain.studyroom

import java.util.UUID
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import javax.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import team.aliens.dms.common.extension.setExcelContentDisposition
import team.aliens.dms.common.extension.toFile
import team.aliens.dms.domain.studyroom.dto.CreateSeatTypeWebRequest
import team.aliens.dms.domain.studyroom.dto.CreateStudyRoomRequest
import team.aliens.dms.domain.studyroom.dto.CreateStudyRoomResponse
import team.aliens.dms.domain.studyroom.dto.CreateStudyRoomWebRequest
import team.aliens.dms.domain.studyroom.dto.CreateTimeSlotWebRequest
import team.aliens.dms.domain.studyroom.dto.CreateTimeSlotWebResponse
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomResponse
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomsResponse
import team.aliens.dms.domain.studyroom.dto.QueryAvailableTimeResponse
import team.aliens.dms.domain.studyroom.dto.QueryCurrentAppliedStudyRoomResponse
import team.aliens.dms.domain.studyroom.dto.QuerySeatTypesResponse
import team.aliens.dms.domain.studyroom.dto.QueryTimeSlotsResponse
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomsResponse
import team.aliens.dms.domain.studyroom.dto.UpdateAvailableTimeWebRequest
import team.aliens.dms.domain.studyroom.dto.UpdateStudyRoomRequest
import team.aliens.dms.domain.studyroom.dto.UpdateStudyRoomWebRequest
import team.aliens.dms.domain.studyroom.dto.UpdateTimeSlotWebRequest
import team.aliens.dms.domain.studyroom.usecase.ApplySeatUseCase
import team.aliens.dms.domain.studyroom.usecase.CreateSeatTypeUseCase
import team.aliens.dms.domain.studyroom.usecase.CreateStudyRoomUseCase
import team.aliens.dms.domain.studyroom.usecase.CreateTimeSlotUseCase
import team.aliens.dms.domain.studyroom.usecase.ExportStudyRoomApplicationStatusUseCase
import team.aliens.dms.domain.studyroom.usecase.ManagerQueryStudyRoomUseCase
import team.aliens.dms.domain.studyroom.usecase.ManagerQueryStudyRoomsUseCase
import team.aliens.dms.domain.studyroom.usecase.QueryAvailableTimeUseCase
import team.aliens.dms.domain.studyroom.usecase.QueryCurrentAppliedStudyRoomUseCase
import team.aliens.dms.domain.studyroom.usecase.QuerySeatTypesUseCase
import team.aliens.dms.domain.studyroom.usecase.QueryTimeSlotsUseCase
import team.aliens.dms.domain.studyroom.usecase.RemoveSeatTypeUseCase
import team.aliens.dms.domain.studyroom.usecase.RemoveStudyRoomUseCase
import team.aliens.dms.domain.studyroom.usecase.RemoveTimeSlotUseCase
import team.aliens.dms.domain.studyroom.usecase.StudentQueryStudyRoomUseCase
import team.aliens.dms.domain.studyroom.usecase.StudentQueryStudyRoomsUseCase
import team.aliens.dms.domain.studyroom.usecase.UnApplySeatUseCase
import team.aliens.dms.domain.studyroom.usecase.UpdateAvailableTimeUseCase
import team.aliens.dms.domain.studyroom.usecase.UpdateStudyRoomUseCase
import team.aliens.dms.domain.studyroom.usecase.UpdateTimeSlotUseCase

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
    private val studentQueryStudyRoomsUseCase: StudentQueryStudyRoomsUseCase,
    private val managerQueryStudyRoomsUseCase: ManagerQueryStudyRoomsUseCase,
    private val removeSeatTypeUseCase: RemoveSeatTypeUseCase,
    private val queryCurrentAppliedStudyRoomUseCase: QueryCurrentAppliedStudyRoomUseCase,
    private val exportStudyRoomApplicationStatusUseCase: ExportStudyRoomApplicationStatusUseCase,
    private val queryTimeSlotsUseCase: QueryTimeSlotsUseCase,
    private val createTimeSlotUseCase: CreateTimeSlotUseCase,
    private val updateTimeSlotUseCase: UpdateTimeSlotUseCase,
    private val removeTimeSlotUseCase: RemoveTimeSlotUseCase
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
    fun getSeatTypes(@RequestParam(name = "study_room_id", required = false) studyRoomId: UUID?): QuerySeatTypesResponse {
        return querySeatTypesUseCase.execute(studyRoomId)
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
    fun applySeat(
        @PathVariable("seat-id") @NotNull seatId: UUID?,
        @RequestParam(name = "time_slot") @NotNull timeSlotId: UUID?
    ) {
        return applySeatUseCase.execute(
            seatId = seatId!!,
            timeSlotId = timeSlotId!!
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/seats")
    fun unApplySeat(@RequestParam(name = "time_slot") @NotNull timeSlotId: UUID?) {
        unApplySeatUseCase.execute(timeSlotId!!)
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
                    availableSex = availableSex!!.name,
                    availableGrade = availableGrade!!,
                    timeSlotIds = timeSlotIds!!,
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
                    availableSex = availableSex!!.name,
                    availableGrade = availableGrade!!,
                    timeSlotIds = timeSlotIds!!,
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
    fun studentGetStudyRoom(
        @PathVariable("study-room-id") @NotNull studyRoomId: UUID?,
        @RequestParam(name = "time_slot") @NotNull timeSlotId: UUID?
    ): StudentQueryStudyRoomResponse {
        return studentQueryStudyRoomUseCase.execute(
            studyRoomId = studyRoomId!!,
            timeSlotId = timeSlotId!!
        )
    }

    @GetMapping("/{study-room-id}/managers")
    fun managerGetStudyRoom(
        @PathVariable("study-room-id") @NotNull studyRoomId: UUID?,
        @RequestParam(name = "time_slot") @NotNull timeSlotId: UUID?
    ): ManagerQueryStudyRoomResponse {
        return managerQueryStudyRoomUseCase.execute(
            studyRoomId = studyRoomId!!,
            timeSlotId = timeSlotId!!
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{study-room-id}")
    fun removeStudyRoom(@PathVariable("study-room-id") @NotNull studyRoomId: UUID?) {
        removeStudyRoomUseCase.execute(studyRoomId!!)
    }

    @GetMapping("/list/students")
    fun studentGetStudyRooms(@RequestParam(name = "time_slot") @NotNull timeSlotId: UUID?): StudentQueryStudyRoomsResponse {
        return studentQueryStudyRoomsUseCase.execute(timeSlotId!!)
    }

    @GetMapping("/list/managers")
    fun managerGetStudyRooms(@RequestParam(name = "time_slot") @NotNull timeSlotId: UUID?): ManagerQueryStudyRoomsResponse {
        return managerQueryStudyRoomsUseCase.execute(timeSlotId!!)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/types/{type-id}")
    fun removeSeatType(@PathVariable("type-id") @NotNull seatTypeId: UUID?) {
        return removeSeatTypeUseCase.execute(seatTypeId!!)
    }

    @GetMapping("/my")
    fun getMyStudyRoom(): QueryCurrentAppliedStudyRoomResponse {
        return queryCurrentAppliedStudyRoomUseCase.execute()
    }

    @GetMapping("/time-slots")
    fun queryTimeSlots(): QueryTimeSlotsResponse {
        return queryTimeSlotsUseCase.execute()
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/time-slots")
    fun createTimeSlotUseCase(
        @RequestBody @Valid request: CreateTimeSlotWebRequest
    ): CreateTimeSlotWebResponse {
        val id = createTimeSlotUseCase.execute(request.startTime!!, request.endTime!!)
        return CreateTimeSlotWebResponse(id)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/time-slots/{time-slot-id}")
    fun updateTimeSlot(
        @PathVariable("time-slot-id") @NotNull timeSlotId: UUID?,
        @RequestBody @Valid request: UpdateTimeSlotWebRequest
    ) {
        updateTimeSlotUseCase.execute(
            timeSlotId = timeSlotId!!,
            startTime = request.startTime!!,
            endTime = request.endTime!!
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/time-slots/{time-slot-id}")
    fun removeTimeSlot(@PathVariable("time-slot-id") @NotNull timeSlotId: UUID?) {
        removeTimeSlotUseCase.execute(timeSlotId!!)
    }

    @GetMapping("/students/file")
    fun exportStudyRoomStudentsApplicationStatus(
        @RequestPart file: MultipartFile?,
        httpResponse: HttpServletResponse
    ): ByteArray {
        val response = exportStudyRoomApplicationStatusUseCase.execute(
            file = file?.toFile()
        )
        httpResponse.setExcelContentDisposition(response.fileName)
        return response.file
    }
}

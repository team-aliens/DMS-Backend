package team.aliens.dms.domain.studyroom.dto

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.studyroom.model.AvailableTime
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.vo.SeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import java.time.LocalTime
import java.util.UUID

data class StudyRoomIdResponse(
    val studyRoomId: UUID,
)

data class StudyRoomResponse(
    val id: UUID,
    val floor: Int,
    val name: String,
    val availableSex: Sex,
    val availableGrade: Int,
    val totalAvailableSeat: Int,
    val eastDescription: String? = null,
    val westDescription: String? = null,
    val southDescription: String? = null,
    val northDescription: String? = null,
    val totalWidthSize: Int? = null,
    val totalHeightSize: Int? = null,
    val inUseHeadcount: Int? = null,
    val seats: List<SeatResponse>? = null,
    val timeSlots: List<TimeSlotResponse>? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val isMine: Boolean? = null,
) {

    companion object {
        fun of(studyRoom: StudyRoom) = studyRoom.run {
            StudyRoomResponse(
                id = id,
                floor = floor,
                name = name,
                availableSex = availableSex,
                availableGrade = availableGrade,
                totalAvailableSeat = availableHeadcount
            )
        }

        fun ofDetail(studyRoom: StudyRoom) = studyRoom.run {
            of(this).copy(
                eastDescription = studyRoom.eastDescription,
                westDescription = studyRoom.westDescription,
                southDescription = studyRoom.southDescription,
                northDescription = studyRoom.northDescription,
                totalWidthSize = studyRoom.widthSize,
                totalHeightSize = studyRoom.heightSize
            )
        }
    }

    fun withSeats(seats: List<SeatApplicationVO>, studentId: UUID? = null) =
        this.copy(
            inUseHeadcount = seats.count { it.studentId != null },
            seats = seats.map { SeatResponse.of(it, studentId) }
        )

    fun withTimeSlots(timeSlots: List<TimeSlot>) =
        this.copy(
            timeSlots = timeSlots.map { TimeSlotResponse.of(it) }
        )

    fun withTimeSlot(timeSlot: TimeSlot) =
        this.copy(
            startTime = timeSlot.startTime,
            endTime = timeSlot.endTime
        )
}

data class SeatResponse(
    val id: UUID,
    val widthLocation: Int,
    val heightLocation: Int,
    val status: SeatStatus,
    val number: Int?,
    val type: SeatTypeResponse?,
    val student: StudyRoomStudentResponse?,
    val isMine: Boolean?,
) {
    companion object {
        fun of(seat: SeatApplicationVO, studentId: UUID? = null) = SeatResponse(
            id = seat.seatId,
            widthLocation = seat.widthLocation,
            heightLocation = seat.heightLocation,
            number = seat.number,
            type = seat.typeId?.let {
                SeatTypeResponse(
                    id = it,
                    name = seat.typeName!!,
                    color = seat.typeColor!!
                )
            },
            status = seat.status,
            student = seat.studentId?.let {
                StudyRoomStudentResponse(
                    id = it,
                    name = seat.studentName!!,
                    gcn = Student.processGcn(seat.studentGrade!!, seat.studentClassRoom!!, seat.studentNumber!!),
                    profileImageUrl = seat.studentProfileImageUrl!!,
                )
            },
            isMine = studentId?.let { it == seat.studentId }
        )
    }
}

data class StudyRoomStudentResponse(
    val id: UUID,
    val name: String,
    val gcn: String? = null,
    val profileImageUrl: String? = null,
)

data class SeatTypeResponse(
    val id: UUID,
    val name: String,
    val color: String,
) {
    companion object {
        fun of(seatType: SeatType) = seatType.run {
            SeatTypeResponse(
                id = id,
                name = name,
                color = color
            )
        }
    }
}

data class SeatTypesResponse(
    val types: List<SeatTypeResponse>,
) {
    companion object {
        fun of(types: List<SeatType>) = SeatTypesResponse(
            types = types.map { SeatTypeResponse.of(it) }
        )
    }
}

data class TimeSlotIdResponse(
    val timeSlotId: UUID,
)

data class TimeSlotResponse(
    val id: UUID,
    val startTime: LocalTime,
    val endTime: LocalTime,
) {
    companion object {
        fun of(timeSlot: TimeSlot) = TimeSlotResponse(
            id = timeSlot.id,
            startTime = timeSlot.startTime,
            endTime = timeSlot.endTime
        )
    }
}

data class TimeSlotsResponse(
    val timeSlots: List<TimeSlotResponse>,
) {
    companion object {
        fun of(timeSlots: List<TimeSlot>) = TimeSlotsResponse(
            timeSlots = timeSlots.map { TimeSlotResponse.of(it) }
        )
    }
}

data class StudyRoomsResponse(
    val studyRooms: List<StudyRoomResponse>,
) {
    companion object {
        fun of(studyRooms: List<StudyRoomVO>) = StudyRoomsResponse(
            studyRooms = studyRooms.map {
                it.run {
                    StudyRoomResponse(
                        id = id,
                        floor = floor,
                        name = name,
                        availableGrade = availableGrade,
                        availableSex = availableSex,
                        inUseHeadcount = inUseHeadcount,
                        totalAvailableSeat = totalAvailableSeat
                    )
                }
            }
        )

        fun of(studyRooms: List<StudyRoomVO>, appliedStudyRoomId: UUID? = null) =
            StudyRoomsResponse(
                studyRooms = of(studyRooms).studyRooms
                    .map {
                        it.copy(isMine = appliedStudyRoomId == it.id)
                    }
            )
    }
}

data class AvailableTimeResponse(
    val startAt: LocalTime,
    val endAt: LocalTime
) {
    companion object {
        fun of(availableTime: AvailableTime) = availableTime.run {
            AvailableTimeResponse(
                startAt = startAt,
                endAt = endAt
            )
        }
    }
}

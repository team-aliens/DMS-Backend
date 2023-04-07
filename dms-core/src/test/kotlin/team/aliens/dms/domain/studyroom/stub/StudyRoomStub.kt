package team.aliens.dms.domain.studyroom.stub

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.TimeSlot
import java.time.LocalTime
import java.util.UUID

internal fun createStudyRoomStub(
    id: UUID = UUID.randomUUID(),
    schoolId: UUID = UUID.randomUUID(),
    name: String = "가온실",
    floor: Int = 1,
    widthSize: Int = 5,
    heightSize: Int = 5,
    availableHeadcount: Int = 10,
    availableSex: Sex = Sex.ALL,
    availableGrade: Int = 0,
    eastDescription: String = "",
    westDescription: String = "",
    southDescription: String = "",
    northDescription: String = ""
) = StudyRoom(
    id = id,
    schoolId = schoolId,
    name = name,
    floor = floor,
    widthSize = widthSize,
    heightSize = heightSize,
    availableHeadcount = availableHeadcount,
    availableSex = availableSex,
    availableGrade = availableGrade,
    eastDescription = eastDescription,
    westDescription = westDescription,
    southDescription = southDescription,
    northDescription = northDescription
)

internal fun createSeatStub(
    id: UUID = UUID.randomUUID(),
    studyRoomId: UUID = UUID.randomUUID(),
    typeId: UUID? = UUID.randomUUID(),
    widthLocation: Int = 2,
    heightLocation: Int = 3,
    number: Int? = 1,
    status: SeatStatus = SeatStatus.AVAILABLE
) = Seat(
    id = id,
    studyRoomId = studyRoomId,
    typeId = typeId,
    widthLocation = widthLocation,
    heightLocation = heightLocation,
    number = number,
    status = status
)

internal fun createTimeSlotStub(
    id: UUID = UUID.randomUUID(),
    schoolId: UUID = UUID.randomUUID(),
    startTime: LocalTime = LocalTime.of(10, 0),
    endTime: LocalTime = LocalTime.of(11, 0)
) = TimeSlot(
    id = id,
    schoolId = schoolId,
    startTime = startTime,
    endTime = endTime
)

internal fun createSeatApplicationStub(
    seatId: UUID = UUID.randomUUID(),
    timeSlotId: UUID = UUID.randomUUID(),
    studentId: UUID = UUID.randomUUID()
) = SeatApplication(
    seatId = seatId,
    timeSlotId = timeSlotId,
    studentId = studentId
)

internal fun createSeatTypeStub(
    id: UUID = UUID.randomUUID(),
    schoolId: UUID = UUID.randomUUID(),
    name: String = "자리",
    color: String = "#FFFFFF",
) = SeatType(
    id = id,
    schoolId = schoolId,
    name = name,
    color = color
)

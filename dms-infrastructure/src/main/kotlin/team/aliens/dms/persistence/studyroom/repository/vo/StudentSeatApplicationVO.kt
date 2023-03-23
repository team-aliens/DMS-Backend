package team.aliens.dms.persistence.studyroom.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatApplicationVO
import java.util.UUID

class StudentSeatApplicationVO @QueryProjection constructor(
    studentId: UUID,
    studyRoomName: String,
    studyRoomFloor: Int,
    seatNumber: Int,
    seatTypeName: String?,
    timeSlotId: UUID
) : StudentSeatApplicationVO(
    studentId = studentId,
    studyRoomName = studyRoomName,
    studyRoomFloor = studyRoomFloor,
    seatNumber = seatNumber,
    seatTypeName = seatTypeName,
    timeSlotId = timeSlotId
)

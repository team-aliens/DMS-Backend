package team.aliens.dms.domain.studyroom.spi.vo

import java.util.UUID

open class StudentSeatApplicationVO(
    val studentId: UUID,
    val studyRoomName: String,
    val studyRoomFloor: Int,
    val seatNumber: Int,
    val seatTypeName: String,
    val timeSlotId: UUID
)

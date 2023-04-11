package team.aliens.dms.domain.studyroom.spi.vo

import java.util.UUID

class StudentSeatInfo(
    val grade: Int,
    val classRoom: Int,
    val number: Int,
    val studentName: String,
    val seats: List<SeatInfo>?
) {
    class SeatInfo(
        val seatFullName: String?,
        val timeSlotId: UUID?
    )
}

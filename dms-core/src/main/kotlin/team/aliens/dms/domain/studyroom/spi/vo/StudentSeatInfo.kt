package team.aliens.dms.domain.studyroom.spi.vo

import java.util.UUID

class StudentSeatInfo(
    val studentGrade: Int,
    val studentClassRoom: Int,
    val studentNumber: Int,
    val studentName: String,
    val seats: List<SeatInfo>?
) {
    class SeatInfo(
        val seatFullName: String?,
        val timeSlotId: UUID?
    )
}

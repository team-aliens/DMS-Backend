package team.aliens.dms.domain.studyroom.spi.vo

import java.util.UUID

class StudentSeatInfo(
    val studentId: UUID,
    val studentName: String,
    val studentGrade: Int,
    val studentClassRoom: Int,
    val studentNumber: Int,
    val seatFullName: String?,
    val timeSlotId: UUID?
)

package team.aliens.dms.domain.volunteer.spi.vo

import java.util.UUID

data class VolunteerScoreWithStudentVO(
    val studentId: UUID,
    val studentName: String,
    val studentGrade: Int,
    val studentClassRoom: Int,
    val studentNumber: Int,
    val assignScore: Int,
    val bonusTotal: Int,
    val minusTotal: Int,
    val schoolId: UUID
)

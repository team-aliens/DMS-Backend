package team.aliens.dms.persistence.volunteer.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerScoreWithStudentVO
import java.util.UUID

data class QueryVolunteerScoreWithStudentVO @QueryProjection constructor(
    val studentId: UUID,
    val studentName: String,
    val studentGrade: Int,
    val studentClassRoom: Int,
    val studentNumber: Int,
    val assignScore: Int,
    val bonusTotal: Int,
    val minusTotal: Int,
    val schoolId: UUID
) {
    fun toDomain() = VolunteerScoreWithStudentVO(
        studentId = studentId,
        studentName = studentName,
        studentGrade = studentGrade,
        studentClassRoom = studentClassRoom,
        studentNumber = studentNumber,
        assignScore = assignScore,
        bonusTotal = bonusTotal,
        minusTotal = minusTotal,
        schoolId = schoolId
    )
}

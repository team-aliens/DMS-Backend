package team.aliens.dms.domain.volunteer.spi.vo

import team.aliens.dms.domain.student.model.Student
import java.util.UUID

open class VolunteerApplicantVO(
    val id: UUID,
    val grade: Int,
    val classRoom: Int,
    val number: Int,
    val name: String
) {
    val gcn = Student.processGcn(grade, classRoom, number)
}

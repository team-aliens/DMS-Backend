package team.aliens.dms.domain.student.spi.vo

import team.aliens.dms.domain.student.model.Student
import java.util.UUID

open class AllStudentsVO(
    val id: UUID,
    val name: String,
    val grade: Int,
    val classRoom: Int,
    val number: Int,
    val profileImageUrl: String
) {
    val gcn = Student.processGcn(grade, classRoom, number)
}

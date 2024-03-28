package team.aliens.dms.domain.outing.spi.vo

import team.aliens.dms.domain.student.model.Student
import java.util.UUID

open class OutingCompanionDetailsVO(
    val id: UUID,
    val studentName: String,
    val studentGrade: Int,
    val studentClassRoom: Int,
    val studentNumber: Int,
    val roomNumber: String
) {
    val studentGcn: String = Student.processGcn(studentGrade, studentClassRoom, studentNumber)
}

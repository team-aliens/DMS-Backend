package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.spi.vo.StudentWithPointVO
import team.aliens.dms.domain.student.model.Student
import java.util.UUID

interface PointQueryStudentPort {

    fun queryStudentById(studentId: UUID): Student?

    fun queryStudentsWithPointHistory(studentIds: List<UUID>): List<StudentWithPointVO>

}
package team.aliens.dms.domain.point.spi

import java.util.UUID
import team.aliens.dms.domain.point.spi.vo.StudentWithPointVO
import team.aliens.dms.domain.student.model.Student

interface PointQueryStudentPort {

    fun queryStudentById(studentId: UUID): Student?

    fun queryStudentByUserId(userId: UUID): Student?

    fun queryStudentsWithPointHistory(studentIds: List<UUID>): List<StudentWithPointVO>
}

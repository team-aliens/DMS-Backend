package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.student.model.Student
import java.util.UUID

interface AuthQueryStudentPort {

    fun queryStudentById(studentId: UUID): Student?
}

package team.aliens.dms.domain.user.spi

import team.aliens.dms.domain.student.model.Student
import java.util.UUID

interface UserQueryStudentPort {

    fun queryStudentById(studentId: UUID): Student?

}
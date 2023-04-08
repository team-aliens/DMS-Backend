package team.aliens.dms.domain.user.spi

import java.util.UUID
import team.aliens.dms.domain.student.model.Student

interface UserQueryStudentPort {

    fun queryStudentById(studentId: UUID): Student?

    fun queryStudentByUserId(userId: UUID): Student?
}

package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.student.model.Student
import java.util.UUID

interface ManagerQueryStudentPort {

    fun queryStudentById(studentId: UUID): Student?

}
package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.student.model.Student
import java.util.UUID

interface TagQueryStudentPort {

    fun queryStudentById(studentId: UUID): Student?
}

package team.aliens.dms.domain.remain.spi

import team.aliens.dms.domain.student.model.Student
import java.util.UUID

interface RemainQueryStudentPort {
    fun queryStudentsBySchoolId(schoolId: UUID): List<Student>
}

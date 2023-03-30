package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.student.model.Student
import java.util.UUID

interface StudyRoomQueryStudentPort {

    fun queryStudentById(studentId: UUID): Student?

    fun queryStudentsBySchoolId(schoolId: UUID): List<Student>
}

package team.aliens.dms.domain.studyroom.spi

import java.util.UUID
import team.aliens.dms.domain.student.model.Student

interface StudyRoomQueryStudentPort {

    fun queryStudentById(studentId: UUID): Student?

    fun queryStudentByUserId(userId: UUID): Student?

    fun queryStudentsBySchoolId(schoolId: UUID): List<Student>
}

package team.aliens.dms.domain.studyroom.spi

import java.util.UUID
import team.aliens.dms.domain.student.model.Student

interface StudyRoomQueryStudentPort {

    fun queryStudentByIdOrNull(studentId: UUID?): Student?

}
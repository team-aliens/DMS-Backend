package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.StudyRoom
import java.util.UUID

interface ManagerQueryStudyRoomPort {

    fun querySeatByStudentId(studentId: UUID): Seat?

    fun queryStudyRoomById(studyRoomId: UUID): StudyRoom?
}

package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.studyroom.model.StudyRoom
import java.util.UUID

interface StudentQueryStudyRoomPort {

    fun queryStudyRoomById(studyRoomId: UUID): StudyRoom?
}

package team.aliens.dms.domain.studyroom.spi

import java.util.UUID
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.StudyRoom

interface QueryStudyRoomPort {

    fun queryStudyRoomById(studyRoomId: UUID): StudyRoom?

    fun querySeatById(seatId: UUID): Seat?

    fun querySeatByStudentId(studentId: UUID): Seat?

    fun existsStudyRoomByFloorAndName(floor: Int, name: String): Boolean

}
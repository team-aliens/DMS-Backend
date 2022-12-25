package team.aliens.dms.domain.studyroom.spi

import java.util.UUID
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatVO
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.vo.ManagerSeatVO

interface QueryStudyRoomPort {

    fun queryStudyRoomById(studyRoomId: UUID): StudyRoom?

    fun querySeatById(seatId: UUID): Seat?

    fun querySeatByStudentId(studentId: UUID): Seat?

    fun existsStudyRoomByFloorAndNameAndSchoolId(floor: Int, name: String, schoolId: UUID): Boolean

    fun queryAllStudentSeatsByStudyRoomId(studyRoomId: UUID): List<StudentSeatVO>

    fun queryAllManagerSeatsByStudyRoomId(studyRoomId: UUID): List<ManagerSeatVO>

}
package team.aliens.dms.domain.studyroom.spi

import java.util.UUID
import team.aliens.dms.domain.studyroom.spi.vo.SeatVO
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO

interface QueryStudyRoomPort {

    fun queryStudyRoomById(studyRoomId: UUID): StudyRoom?

    fun querySeatById(seatId: UUID): Seat?

    fun querySeatByStudentId(studentId: UUID): Seat?

    fun existsStudyRoomByFloorAndNameAndSchoolId(floor: Int, name: String, schoolId: UUID): Boolean

    fun queryAllSeatsByStudyRoomId(studyRoomId: UUID): List<SeatVO>

    fun queryAllStudyRoomsBySchoolId(schoolId: UUID): List<StudyRoomVO>

    fun countSeatByStudyRoomId(studyRoomId: UUID): Int

    fun querySeatByStudyRoomId(studyRoomId: UUID): Seat?

}
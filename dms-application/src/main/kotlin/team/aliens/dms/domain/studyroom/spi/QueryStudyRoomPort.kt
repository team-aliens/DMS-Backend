package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import team.aliens.dms.domain.studyroom.spi.vo.SeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import java.util.UUID

interface QueryStudyRoomPort {

    fun queryStudyRoomById(studyRoomId: UUID): StudyRoom?

    fun querySeatById(seatId: UUID): Seat?

    fun querySeatApplicationsByStudentId(studentId: UUID): List<SeatApplication>

    fun queryAllSeatsById(seatIds: List<UUID>): List<Seat>

    fun existsStudyRoomByFloorAndNameAndSchoolId(floor: Int, name: String, schoolId: UUID): Boolean

    fun queryAllSeatApplicationVOsByStudyRoomIdAndTimeSlotId(studyRoomId: UUID, timeSlotId: UUID?): List<SeatApplicationVO>

    fun queryAllStudyRoomsByTimeSlotId(timeSlotId: UUID?): List<StudyRoomVO>

    fun queryTimeSlotsBySchoolId(schoolId: UUID): List<StudyRoomTimeSlot>

    fun queryTimeSlotById(timeSlotId: UUID): StudyRoomTimeSlot?

    fun existsTimeSlotById(timeSlotId: UUID): Boolean

    fun existsTimeSlotsBySchoolId(schoolId: UUID): Boolean

    fun queryAllSeatApplicationByTimeSlotId(timeSlotId: UUID?): List<SeatApplication>

    fun existsSeatApplicationBySeatIdAndTimeSlotId(seatId: UUID, timeSlotId: UUID?): Boolean
}

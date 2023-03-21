package team.aliens.dms.domain.studyroom.spi

import java.time.LocalTime
import java.util.UUID
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.vo.SeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO

interface QueryStudyRoomPort {

    fun queryStudyRoomById(studyRoomId: UUID): StudyRoom?

    fun querySeatById(seatId: UUID): Seat?

    fun querySeatApplicationsByStudentId(studentId: UUID): List<SeatApplication>

    fun queryAllSeatsById(seatIds: List<UUID>): List<Seat>

    fun existsStudyRoomByFloorAndNameAndSchoolId(floor: Int, name: String, schoolId: UUID): Boolean

    fun queryAllSeatApplicationVOsByStudyRoomIdAndTimeSlotId(studyRoomId: UUID, timeSlotId: UUID): List<SeatApplicationVO>

    fun queryAllStudyRoomsByTimeSlotId(timeSlotId: UUID): List<StudyRoomVO>

    fun queryTimeSlotsBySchoolId(schoolId: UUID): List<TimeSlot>

    fun queryTimeSlotById(timeSlotId: UUID): TimeSlot?

    fun queryTimeSlotsBySchoolIdAndStudyRoomId(schoolId: UUID, studyRoomId: UUID): List<TimeSlot>

    fun existsSeatApplicationBySeatIdAndTimeSlotId(seatId: UUID, timeSlotId: UUID): Boolean

    fun existsStudyRoomTimeSlotByStudyRoomIdAndTimeSlotId(studyRoomId: UUID, timeSlotId: UUID): Boolean

    fun existsTimeSlotByStartTimeAndEndTime(startTime: LocalTime, endTime: LocalTime): Boolean

    fun existsStudyRoomTimeSlotByTimeSlotId(timeSlotId: UUID): Boolean

}

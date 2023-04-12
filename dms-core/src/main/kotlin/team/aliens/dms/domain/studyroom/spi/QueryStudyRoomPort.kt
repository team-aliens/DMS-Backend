package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.vo.SeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import java.time.LocalTime
import java.util.UUID

interface QueryStudyRoomPort {

    fun queryStudyRoomById(studyRoomId: UUID): StudyRoom?

    fun querySeatById(seatId: UUID): Seat?

    fun querySeatApplicationsByStudentId(studentId: UUID): List<SeatApplication>

    fun querySeatApplicationsByStudentIdAndTimeSlotId(studentId: UUID, timeSlotId: UUID): SeatApplication?

    fun queryAllSeatsById(seatIds: List<UUID>): List<Seat>

    fun existsStudyRoomByFloorAndNameAndSchoolId(floor: Int, name: String, schoolId: UUID): Boolean

    fun queryAllSeatApplicationVOsByStudyRoomIdAndTimeSlotId(studyRoomId: UUID, timeSlotId: UUID): List<SeatApplicationVO>

    fun queryAllStudyRoomsByTimeSlotId(timeSlotId: UUID): List<StudyRoomVO>

    fun queryAllStudyRoomsByTimeSlotIdAndGradeAndSex(timeSlotId: UUID, grade: Int, sex: Sex): List<StudyRoomVO>

    fun queryTimeSlotsBySchoolId(schoolId: UUID): List<TimeSlot>

    fun queryTimeSlotById(timeSlotId: UUID): TimeSlot?

    fun queryTimeSlotsBySchoolIdAndStudyRoomId(schoolId: UUID, studyRoomId: UUID): List<TimeSlot>

    fun existsSeatApplicationBySeatIdAndTimeSlotId(seatId: UUID, timeSlotId: UUID): Boolean

    fun existsStudyRoomTimeSlotByStudyRoomIdAndTimeSlotId(studyRoomId: UUID, timeSlotId: UUID): Boolean

    fun existsTimeSlotByStartTimeAndEndTime(startTime: LocalTime, endTime: LocalTime): Boolean

    fun existsStudyRoomTimeSlotByTimeSlotId(timeSlotId: UUID): Boolean

    fun querySeatApplicationsByStudentIdIn(studentIds: List<UUID>): List<StudentSeatApplicationVO>
}

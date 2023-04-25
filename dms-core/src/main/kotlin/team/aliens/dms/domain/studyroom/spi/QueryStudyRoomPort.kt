package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.model.AvailableTime
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.vo.SeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import java.time.LocalTime
import java.util.UUID

interface QueryStudyRoomPort {

    fun queryStudyRoomById(studyRoomId: UUID): StudyRoom?

    fun querySeatApplicationsByStudentId(studentId: UUID): List<SeatApplication>

    fun querySeatApplicationsByStudentIdAndTimeSlotId(studentId: UUID, timeSlotId: UUID): SeatApplication?

    fun queryAllSeatsById(seatIds: List<UUID>): List<Seat>

    fun querySeatById(seatId: UUID): Seat?

    fun queryAvailableTimeBySchoolId(schoolId: UUID): AvailableTime?

    fun existsStudyRoomByFloorAndNameAndSchoolId(floor: Int, name: String, schoolId: UUID): Boolean

    fun queryAllSeatTypeBySchoolId(schoolId: UUID): List<SeatType>

    fun queryAllSeatTypeByStudyRoomId(studyRoomId: UUID): List<SeatType>

    fun existsSeatTypeByNameAndSchoolId(name: String, schoolId: UUID): Boolean

    fun querySeatTypeById(seatTypeId: UUID): SeatType?

    fun queryAllSeatApplicationVOsByStudyRoomIdAndTimeSlotId(studyRoomId: UUID, timeSlotId: UUID): List<SeatApplicationVO>

    fun queryAllStudyRoomsByTimeSlotIdAndGradeAndSex(timeSlotId: UUID, grade: Int? = null, sex: Sex? = null): List<StudyRoomVO>

    fun queryTimeSlotById(timeSlotId: UUID): TimeSlot?

    fun queryTimeSlotsBySchoolIdAndStudyRoomId(schoolId: UUID, studyRoomId: UUID?): List<TimeSlot>

    fun existsSeatApplicationBySeatIdAndTimeSlotId(seatId: UUID, timeSlotId: UUID): Boolean

    fun existsStudyRoomTimeSlotByStudyRoomIdAndTimeSlotId(studyRoomId: UUID, timeSlotId: UUID): Boolean

    fun existsTimeSlotByStartTimeAndEndTimeAndSchoolId(startTime: LocalTime, endTime: LocalTime, schoolId: UUID): Boolean

    fun existsStudyRoomTimeSlotByTimeSlotId(timeSlotId: UUID): Boolean

    fun querySeatApplicationsByStudentIdIn(studentIds: List<UUID>): List<StudentSeatApplicationVO>

    fun existsSeatBySeatTypeId(seatTypeId: UUID): Boolean
}

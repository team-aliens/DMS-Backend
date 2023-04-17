package team.aliens.dms.domain.studyroom.service

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.model.AvailableTime
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.vo.SeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatInfo
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import java.io.File
import java.util.UUID

interface GetStudyRoomService {

    fun getStudyRoom(studyRoomId: UUID, schoolId: UUID): StudyRoom

    fun getStudyRoomBySeatId(seatId: UUID): StudyRoom

    fun getTimeSlot(timeSlotId: UUID, schoolId: UUID): TimeSlot

    fun getTimeSlots(schoolId: UUID, studyRoomId: UUID? = null): List<TimeSlot>

    fun getSeatById(seatId: UUID): Seat

    fun getAppliedSeat(studentId: UUID, timeSlotId: UUID): Seat?

    fun getSeatTypes(schoolId: UUID, studyRoomId: UUID? = null): List<SeatType>

    fun getAppliedSeatApplications(studentId: UUID): List<SeatApplication>

    fun queryAvailableTimeBySchoolId(schoolId: UUID): AvailableTime

    fun getStudyRoomVOs(timeSlotId: UUID, grade: Int? = null, sex: Sex? = null): List<StudyRoomVO>

    fun getSeatApplicationVOs(studyRoomId: UUID, timeSlotId: UUID): List<SeatApplicationVO>

    fun getStudentSeatInfos(schoolId: UUID): List<StudentSeatInfo>

    fun getStudyRoomApplicationStatusFile(
        file: File?,
        timeSlots: List<TimeSlot>,
        studentSeats: List<StudentSeatInfo>
    ): ByteArray
}

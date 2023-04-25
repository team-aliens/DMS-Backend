package team.aliens.dms.domain.studyroom.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.studyroom.exception.AvailableTimeNotFoundException
import team.aliens.dms.domain.studyroom.exception.SeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.model.AvailableTime
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.vo.SeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatInfo
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import java.io.File
import java.util.UUID

@Service
class GetStudyRoomServiceImpl(
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val queryStudentPort: QueryStudentPort,
    private val writeFilePort: WriteFilePort,
) : GetStudyRoomService {

    override fun getStudyRoom(studyRoomId: UUID, schoolId: UUID): StudyRoom {
        return (queryStudyRoomPort.queryStudyRoomById(studyRoomId) ?: throw TimeSlotNotFoundException)
            .apply { validateSameSchool(this.schoolId, schoolId) }
    }

    override fun getStudyRoomBySeatId(seatId: UUID): StudyRoom {
        val seat = queryStudyRoomPort.querySeatById(seatId) ?: throw SeatNotFoundException
        return queryStudyRoomPort.queryStudyRoomById(seat.studyRoomId) ?: throw StudyRoomNotFoundException
    }

    override fun getTimeSlot(timeSlotId: UUID, schoolId: UUID): TimeSlot {
        return (queryStudyRoomPort.queryTimeSlotById(timeSlotId) ?: throw TimeSlotNotFoundException)
            .apply { validateSameSchool(this.schoolId, schoolId) }
    }

    override fun getTimeSlots(schoolId: UUID, studyRoomId: UUID?): List<TimeSlot> {
        return queryStudyRoomPort.queryTimeSlotsBySchoolIdAndStudyRoomId(schoolId, studyRoomId)
    }

    override fun getSeatById(seatId: UUID): Seat {
        return queryStudyRoomPort.querySeatById(seatId) ?: throw TimeSlotNotFoundException
    }

    override fun getAppliedSeat(studentId: UUID, timeSlotId: UUID): Seat? {
        val seatApplication = queryStudyRoomPort.querySeatApplicationsByStudentIdAndTimeSlotId(studentId, timeSlotId)
        return seatApplication?.seatId?.let { queryStudyRoomPort.querySeatById(it) }
    }

    override fun getSeatTypes(schoolId: UUID, studyRoomId: UUID?): List<SeatType> {
        return studyRoomId?.let {
            queryStudyRoomPort.queryAllSeatTypeByStudyRoomId(studyRoomId)
        } ?: queryStudyRoomPort.queryAllSeatTypeBySchoolId(schoolId)
    }

    override fun getAppliedSeatApplications(studentId: UUID): List<SeatApplication> {
        return queryStudyRoomPort.querySeatApplicationsByStudentId(studentId)
    }

    override fun getAvailableTime(schoolId: UUID): AvailableTime {
        return queryStudyRoomPort.queryAvailableTimeBySchoolId(schoolId) ?: throw AvailableTimeNotFoundException
    }

    override fun getStudyRoomVOs(timeSlotId: UUID, grade: Int?, sex: Sex?): List<StudyRoomVO> {
        return queryStudyRoomPort.queryAllStudyRoomsByTimeSlotIdAndGradeAndSex(timeSlotId, grade, sex)
    }

    override fun getSeatApplicationVOs(studyRoomId: UUID, timeSlotId: UUID): List<SeatApplicationVO> {
        return queryStudyRoomPort.queryAllSeatApplicationVOsByStudyRoomIdAndTimeSlotId(studyRoomId, timeSlotId)
    }

    override fun getStudentSeatInfos(schoolId: UUID): List<StudentSeatInfo> {
        val students = queryStudentPort.queryStudentsBySchoolId(schoolId)
        val studentSeatApplicationsMap = getStudentSeatApplicationsMap(students)

        return students.map { student ->
            StudentSeatInfo(
                studentName = student.name,
                studentGrade = student.grade,
                studentClassRoom = student.classRoom,
                studentNumber = student.number,
                seats = studentSeatApplicationsMap[student.id]?.map {
                    StudentSeatInfo.SeatInfo(
                        seatFullName = StudyRoom.precessName(it.studyRoomFloor, it.studyRoomName) +
                            " " + Seat.processName(it.seatNumber, it.seatTypeName),
                        timeSlotId = it.timeSlotId
                    )
                }
            )
        }
    }

    private fun getStudentSeatApplicationsMap(students: List<Student>): MutableMap<UUID, MutableList<StudentSeatApplicationVO>> =
        mutableMapOf<UUID, MutableList<StudentSeatApplicationVO>>().apply {
            queryStudyRoomPort.querySeatApplicationsByStudentIdIn(
                studentIds = students.map { it.id }
            ).map {
                get(it.studentId)
                    ?.run { this.add(it) } ?: put(it.studentId, mutableListOf(it))
            }
        }

    override fun getStudyRoomApplicationStatusFile(
        file: File?,
        timeSlots: List<TimeSlot>,
        studentSeats: List<StudentSeatInfo>,
    ): ByteArray {
        return file?.let {
            writeFilePort.addStudyRoomApplicationStatusExcelFile(
                baseFile = file,
                timeSlots = timeSlots,
                studentSeatsMap = studentSeats.associateBy {
                    Pair(
                        Student.processGcn(it.studentGrade, it.studentClassRoom, it.studentNumber),
                        it.studentName
                    )
                }
            )
        } ?: writeFilePort.writeStudyRoomApplicationStatusExcelFile(
            timeSlots = timeSlots,
            studentSeats = studentSeats
        )
    }
}

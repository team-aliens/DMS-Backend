package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.file.model.File
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.studyroom.dto.ExportStudyRoomApplicationStatusResponse
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatInfo
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime
import java.util.UUID

@ReadOnlyUseCase
class ExportStudyRoomApplicationStatusUseCase(
    private val userService: UserService,
    private val schoolService: SchoolService,
    private val queryStudentPort: QueryStudentPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val writeFilePort: WriteFilePort,
) {

    fun execute(file: java.io.File?): ExportStudyRoomApplicationStatusResponse {

        val user = userService.getCurrentUser()

        val students = queryStudentPort.queryStudentsBySchoolId(user.schoolId)
        val studentSeatApplicationsMap = getStudentSeatApplicationsMap(students)

        val studentSeats = students.map { student ->
            StudentSeatInfo(
                studentGrade = student.grade,
                studentClassRoom = student.classRoom,
                studentNumber = student.number,
                studentName = student.name,
                seats = studentSeatApplicationsMap[student.id]?.map {
                    StudentSeatInfo.SeatInfo(
                        seatFullName = StudyRoom.precessName(it.studyRoomFloor, it.studyRoomName) +
                            " " + Seat.processName(it.seatNumber, it.seatTypeName),
                        timeSlotId = it.timeSlotId
                    )
                }
            )
        }

        val timeSlots = queryStudyRoomPort.queryTimeSlotsBySchoolId(user.schoolId)
        val school = schoolService.getSchoolById(user.schoolId)

        return ExportStudyRoomApplicationStatusResponse(
            file = getStudyRoomApplicationStatusFile(file, timeSlots, studentSeats),
            fileName = getFileName(school.name)
        )
    }

    private fun getStudentSeatApplicationsMap(students: List<Student>) =
        mutableMapOf<UUID, MutableList<StudentSeatApplicationVO>>().apply {
            queryStudyRoomPort.querySeatApplicationsByStudentIdIn(
                studentIds = students.map { it.id }
            ).map {
                get(it.studentId)
                    ?.run { this.add(it) } ?: put(it.studentId, mutableListOf(it))
            }
        }

    private fun getStudyRoomApplicationStatusFile(
        file: java.io.File?,
        timeSlots: List<TimeSlot>,
        studentSeats: List<StudentSeatInfo>,
    ) = file?.let {
        writeFilePort.addStudyRoomApplicationStatusExcelFile(
            baseFile = file,
            timeSlots = timeSlots,
            studentSeatsMap = studentSeats.associateBy {
                Pair(Student.processGcn(it.studentGrade, it.studentClassRoom, it.studentNumber), it.studentName)
            }
        )
    } ?: writeFilePort.writeStudyRoomApplicationStatusExcelFile(
        timeSlots = timeSlots,
        studentSeats = studentSeats
    )

    private fun getFileName(schoolName: String) =
        "${schoolName.replace(" ", "")}_자습실_신청상태_${LocalDateTime.now().format(File.FILE_DATE_FORMAT)}"
}

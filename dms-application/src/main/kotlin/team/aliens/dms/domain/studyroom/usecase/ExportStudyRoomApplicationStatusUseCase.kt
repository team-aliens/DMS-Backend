package team.aliens.dms.domain.studyroom.usecase

import java.time.LocalDateTime
import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.file.model.File
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.studyroom.dto.ExportStudyRoomApplicationStatusResponse
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQuerySchoolPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatInfo
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class ExportStudyRoomApplicationStatusUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val querySchoolPort: StudyRoomQuerySchoolPort,
    private val queryStudentPort: StudyRoomQueryStudentPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val writeFilePort: WriteFilePort,
) {

    fun execute(file: java.io.File?): ExportStudyRoomApplicationStatusResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val students = queryStudentPort.queryStudentsBySchoolId(manager.schoolId)
        val studentSeatApplicationsMap = getStudentSeatApplicationsMap(students)

        val studentSeats = students.map { student ->
            StudentSeatInfo(
                grade = student.grade,
                classRoom = student.classRoom,
                number = student.number,
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

        val timeSlots = queryStudyRoomPort.queryTimeSlotsBySchoolId(manager.schoolId)
        val school = querySchoolPort.querySchoolById(manager.schoolId) ?: throw SchoolNotFoundException

        return ExportStudyRoomApplicationStatusResponse(
            file = getStudyRoomApplicationStatusFile(file, timeSlots, studentSeats),
            fileName = getFileName(school.name)
        )
    }

    private fun getStudentSeatApplicationsMap(students: List<Student>): MutableMap<UUID, MutableList<StudentSeatApplicationVO>> {
        val map = mutableMapOf<UUID, MutableList<StudentSeatApplicationVO>>()
        queryStudyRoomPort.querySeatApplicationsByStudentIdIn(
            studentIds = students.map { it.id }
        ).map {
            map[it.studentId]
                ?.run { this.add(it) } ?: map.put(it.studentId, mutableListOf(it))
        }
        return map
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
                Pair(Student.processGcn(it.grade, it.classRoom, it.number), it.studentName)
            }
        )
    } ?: writeFilePort.writeStudyRoomApplicationStatusExcelFile(
        timeSlots = timeSlots,
        studentSeats = studentSeats
    )

    private fun getFileName(schoolName: String) =
        "${schoolName.replace(" ", "")}_자습실_신청상태_${LocalDateTime.now().format(File.FILE_DATE_FORMAT)}"
}

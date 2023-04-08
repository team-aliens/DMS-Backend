package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomsResponse
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomsResponse.StudyRoomElement
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class StudentQueryStudyRoomsUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val queryStudentPort: StudyRoomQueryStudentPort,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {

    fun execute(timeSlotId: UUID): StudentQueryStudyRoomsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException
        val student = queryStudentPort.queryStudentByUserId(currentUserId) ?: throw StudentNotFoundException
        val timeSlot = queryStudyRoomPort.queryTimeSlotById(timeSlotId) ?: throw TimeSlotNotFoundException

        validateSameSchool(timeSlot.schoolId, user.schoolId)

        val seatApplications = queryStudyRoomPort.querySeatApplicationsByStudentId(currentUserId)
        val userStudyRoomIds = queryStudyRoomPort.queryAllSeatsById(seatApplications.map { it.seatId }).map { it.studyRoomId }

        val studyRooms = queryStudyRoomPort.queryAllStudyRoomsByTimeSlotIdAndGradeAndSex(timeSlotId, student.grade, student.sex)
            .map {
                StudyRoomElement(
                    id = it.id,
                    floor = it.floor,
                    name = it.name,
                    availableGrade = it.availableGrade,
                    availableSex = it.availableSex,
                    inUseHeadcount = it.inUseHeadcount,
                    totalAvailableSeat = it.totalAvailableSeat,
                    isMine = userStudyRoomIds.contains(it.id)
                )
            }

        return StudentQueryStudyRoomsResponse(
            studyRooms = studyRooms
        )
    }
}

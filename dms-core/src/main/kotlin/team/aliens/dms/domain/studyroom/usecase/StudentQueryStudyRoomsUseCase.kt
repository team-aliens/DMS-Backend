package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomsResponse
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomsResponse.StudyRoomElement
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.util.UUID

@ReadOnlyUseCase
class StudentQueryStudyRoomsUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryStudentPort: QueryStudentPort,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {

    fun execute(timeSlotId: UUID): StudentQueryStudyRoomsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException
        val student = queryStudentPort.queryStudentByUserId(currentUserId) ?: throw StudentNotFoundException
        val timeSlot = queryStudyRoomPort.queryTimeSlotById(timeSlotId) ?: throw TimeSlotNotFoundException

        validateSameSchool(timeSlot.schoolId, user.schoolId)

        val appliedSeat = getAppliedSeatApplication(currentUserId, timeSlotId)
        val studyRooms =
            queryStudyRoomPort.queryAllStudyRoomsByTimeSlotIdAndGradeAndSex(timeSlotId, student.grade, student.sex)
                .map {
                    StudyRoomElement(
                        id = it.id,
                        floor = it.floor,
                        name = it.name,
                        availableGrade = it.availableGrade,
                        availableSex = it.availableSex,
                        inUseHeadcount = it.inUseHeadcount,
                        totalAvailableSeat = it.totalAvailableSeat,
                        isMine = appliedSeat?.studyRoomId == it.id
                    )
                }

        return StudentQueryStudyRoomsResponse(
            studyRooms = studyRooms
        )
    }

    private fun getAppliedSeatApplication(currentUserId: UUID, timeSlotId: UUID): Seat? {
        val seatApplication =
            queryStudyRoomPort.querySeatApplicationsByStudentIdAndTimeSlotId(currentUserId, timeSlotId)
        return seatApplication?.seatId?.let { queryStudyRoomPort.querySeatById(it) }
    }
}

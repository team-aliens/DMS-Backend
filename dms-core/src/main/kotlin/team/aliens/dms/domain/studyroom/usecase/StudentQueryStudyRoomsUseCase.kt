package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomsResponse
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomsResponse.StudyRoomElement
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort

@ReadOnlyUseCase
class StudentQueryStudyRoomsUseCase(
    private val studentService: StudentService,
    private val queryStudyRoomPort: QueryStudyRoomPort,
) {

    fun execute(timeSlotId: UUID): StudentQueryStudyRoomsResponse {

        val student = studentService.getCurrentStudent()
        val timeSlot = queryStudyRoomPort.queryTimeSlotById(timeSlotId) ?: throw TimeSlotNotFoundException

        validateSameSchool(timeSlot.schoolId, student.schoolId)

        val appliedSeat = getAppliedSeatApplication(student.id, timeSlotId)
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

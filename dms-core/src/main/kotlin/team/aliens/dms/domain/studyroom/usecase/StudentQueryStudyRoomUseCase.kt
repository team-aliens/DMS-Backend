package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import java.util.UUID

@ReadOnlyUseCase
class StudentQueryStudyRoomUseCase(
    private val studentService: StudentService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(studyRoomId: UUID, timeSlotId: UUID): StudentQueryStudyRoomResponse {

        val student = studentService.getCurrentStudent()

        val studyRoom = studyRoomService.getStudyRoom(studyRoomId, student.schoolId)
        val timeSlot = studyRoomService.getTimeSlot(timeSlotId, student.schoolId).apply {
            studyRoomService.checkStudyRoomTimeSlotExistsById(studyRoomId, id)
        }
        val seats = studyRoomService.getSeatApplicationVOs(studyRoomId, timeSlot.id)

        return StudentQueryStudyRoomResponse(
            studyRoom = studyRoom,
            timeSlot = timeSlot,
            seats = seats,
            studentId = studyRoomId
        )
    }
}

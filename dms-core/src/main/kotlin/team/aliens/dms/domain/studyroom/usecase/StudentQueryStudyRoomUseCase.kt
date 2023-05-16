package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.studyroom.dto.StudyRoomResponse
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import java.util.UUID

@ReadOnlyUseCase
class StudentQueryStudyRoomUseCase(
    private val studentService: StudentService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(studyRoomId: UUID, timeSlotId: UUID): StudyRoomResponse {

        val student = studentService.getCurrentStudent()

        val studyRoom = studyRoomService.getStudyRoom(studyRoomId)
        val timeSlot = studyRoomService.getTimeSlot(timeSlotId)
        studyRoomService.checkStudyRoomTimeSlotExistsById(studyRoomId, timeSlotId)

        val seats = studyRoomService.getSeatApplicationVOs(studyRoomId, timeSlot.id)

        return StudyRoomResponse
            .ofDetail(studyRoom)
            .withTimeSlot(timeSlot)
            .withSeats(seats, student.id)
    }
}

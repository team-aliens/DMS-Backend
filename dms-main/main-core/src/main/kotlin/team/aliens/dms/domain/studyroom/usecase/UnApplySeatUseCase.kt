package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import java.util.UUID

@UseCase
class UnApplySeatUseCase(
    private val studentService: StudentService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(seatId: UUID, timeSlotId: UUID) {
        val student = studentService.getCurrentStudent()
        studyRoomService.deleteSeatApplicationBySeatIdAndStudentIdAndTimeSlotId(seatId, student.id, timeSlotId)
    }
}

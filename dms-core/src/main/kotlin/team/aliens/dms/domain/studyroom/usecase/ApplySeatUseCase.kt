package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import java.util.UUID

@UseCase
class ApplySeatUseCase(
    private val studentService: StudentService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(seatId: UUID, timeSlotId: UUID) {

        val student = studentService.getCurrentStudent()

        val seat = studyRoomService.getSeatById(seatId)
            .apply { checkAvailable() }

        val studyRoom = studyRoomService.getStudyRoom(seat.studyRoomId, student.schoolId)
            .apply { checkIsAvailableGradeAndSex(student.grade, student.sex) }
        studyRoomService.checkStudyRoomApplicationTimeAvailable(studyRoom.schoolId)

        val timeSlot = studyRoomService.getTimeSlot(timeSlotId, student.schoolId)

        studyRoomService.saveSeatApplication(
            SeatApplication(
                seatId = seat.id,
                timeSlotId = timeSlot.id,
                studentId = student.id
            )
        )
    }
}

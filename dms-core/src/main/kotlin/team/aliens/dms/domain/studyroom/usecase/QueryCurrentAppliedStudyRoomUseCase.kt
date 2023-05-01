package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.studyroom.exception.AppliedSeatNotFoundException
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.service.StudyRoomService

@ReadOnlyUseCase
class QueryCurrentAppliedStudyRoomUseCase(
    private val studentService: StudentService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(): StudyRoom {

        val student = studentService.getCurrentStudent()

        val seatApplication = studyRoomService.getAppliedSeatApplications(student.id).run {
            if (isEmpty()) throw AppliedSeatNotFoundException
            else get(0)
        }

        return studyRoomService.getStudyRoomBySeatId(seatApplication.seatId)
    }
}

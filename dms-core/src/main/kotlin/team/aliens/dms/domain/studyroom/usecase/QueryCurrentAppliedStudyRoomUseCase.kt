package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.studyroom.dto.QueryCurrentAppliedStudyRoomResponse
import team.aliens.dms.domain.studyroom.exception.AppliedSeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.SeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryCurrentAppliedStudyRoomUseCase(
    private val studentService: StudentService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(): QueryCurrentAppliedStudyRoomResponse {

        val student = studentService.getCurrentStudent()

        val seatApplication = studyRoomService.getAppliedSeatApplications(student.id).run {
            if (isEmpty()) throw AppliedSeatNotFoundException
            else get(0)
        }

        val studyRoom = studyRoomService.getStudyRoomBySeatId(seatApplication.seatId)
        return QueryCurrentAppliedStudyRoomResponse(
            floor = studyRoom.floor,
            name = studyRoom.name
        )
    }
}

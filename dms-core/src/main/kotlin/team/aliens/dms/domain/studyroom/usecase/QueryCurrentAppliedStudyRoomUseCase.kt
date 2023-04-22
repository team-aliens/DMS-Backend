package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.studyroom.dto.QueryCurrentAppliedStudyRoomResponse
import team.aliens.dms.domain.studyroom.exception.AppliedSeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.SeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort

@ReadOnlyUseCase
class QueryCurrentAppliedStudyRoomUseCase(
    private val studentService: StudentService,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {

    fun execute(): QueryCurrentAppliedStudyRoomResponse {

        val student = studentService.getCurrentStudent()

        val seatApplication = queryStudyRoomPort.querySeatApplicationsByStudentId(student.id).run {
            if (isEmpty()) throw AppliedSeatNotFoundException
            else get(0)
        }
        val seat = queryStudyRoomPort.querySeatById(seatApplication.seatId) ?: throw SeatNotFoundException
        val studyRoom = queryStudyRoomPort.queryStudyRoomById(seat.studyRoomId) ?: throw StudyRoomNotFoundException

        return QueryCurrentAppliedStudyRoomResponse(
            floor = studyRoom.floor,
            name = studyRoom.name
        )
    }
}

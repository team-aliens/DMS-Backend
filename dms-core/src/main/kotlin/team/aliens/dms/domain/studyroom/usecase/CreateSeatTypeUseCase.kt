package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.service.StudyRoomService

@UseCase
class CreateSeatTypeUseCase(
    private val studentService: StudentService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(name: String, color: String) {
        val user = studentService.getCurrentStudent()

        studyRoomService.createSeatType(
            SeatType(
                schoolId = user.schoolId,
                name = name,
                color = color
            )
        )
    }
}

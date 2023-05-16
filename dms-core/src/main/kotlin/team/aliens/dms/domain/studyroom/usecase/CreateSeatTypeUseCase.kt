package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class CreateSeatTypeUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(name: String, color: String) {
        val user = userService.getCurrentStudent()

        studyRoomService.saveSeatType(
            SeatType(
                schoolId = user.schoolId,
                name = name,
                color = color
            )
        )
    }
}

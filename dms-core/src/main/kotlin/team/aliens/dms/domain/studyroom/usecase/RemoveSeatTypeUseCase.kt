package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class RemoveSeatTypeUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(seatTypeId: UUID) {

        val user = userService.getCurrentUser()

        studyRoomService.deleteSeatType(
            seatTypeId = seatTypeId,
            schoolId = user.schoolId
        )
    }
}

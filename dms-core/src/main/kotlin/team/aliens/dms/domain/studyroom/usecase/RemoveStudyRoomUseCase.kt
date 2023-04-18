package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class RemoveStudyRoomUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(studyRoomId: UUID) {

        val user = userService.getCurrentUser()

        val studyRoom = studyRoomService.getStudyRoom(studyRoomId, user.schoolId)
        studyRoomService.deleteStudyRoom(studyRoom.id)
    }
}

package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import java.util.UUID

@UseCase
class RemoveStudyRoomUseCase(
    private val studyRoomService: StudyRoomService
) {

    fun execute(studyRoomId: UUID) {

        val studyRoom = studyRoomService.getStudyRoom(studyRoomId)
        studyRoomService.deleteStudyRoom(studyRoom.id)
    }
}

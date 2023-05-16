package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalTime
import java.util.UUID

@UseCase
class UpdateTimeSlotUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(timeSlotId: UUID, startTime: LocalTime, endTime: LocalTime) {

        val user = userService.getCurrentUser()

        val timeSlot = studyRoomService.getTimeSlot(timeSlotId)
        studyRoomService.saveTimeSlot(
            timeSlot.copy(
                startTime = startTime,
                endTime = endTime
            )
        )
    }
}

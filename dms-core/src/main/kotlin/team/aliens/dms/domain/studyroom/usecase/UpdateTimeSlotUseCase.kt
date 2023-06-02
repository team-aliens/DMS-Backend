package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import java.time.LocalTime
import java.util.UUID

@UseCase
class UpdateTimeSlotUseCase(
    private val studyRoomService: StudyRoomService
) {

    fun execute(timeSlotId: UUID, startTime: LocalTime, endTime: LocalTime) {

        val timeSlot = studyRoomService.getTimeSlot(timeSlotId)
        studyRoomService.updateTimeSlot(
            timeSlot.copy(
                startTime = startTime,
                endTime = endTime
            )
        )
    }
}

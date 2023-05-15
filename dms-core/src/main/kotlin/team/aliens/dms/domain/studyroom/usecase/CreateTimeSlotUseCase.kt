package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.dto.TimeSlotIdResponse
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalTime

@UseCase
class CreateTimeSlotUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(startTime: LocalTime, endTime: LocalTime): TimeSlotIdResponse {

        val user = userService.getCurrentUser()

        val timeSlot = studyRoomService.createTimeSlot(
            TimeSlot(
                schoolId = user.schoolId,
                startTime = startTime,
                endTime = endTime
            )
        )
        return TimeSlotIdResponse(timeSlot.id)
    }
}

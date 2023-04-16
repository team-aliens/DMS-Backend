package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.QueryTimeSlotsResponse
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryTimeSlotsUseCase(
    private val userService: UserService,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {
    fun execute(): QueryTimeSlotsResponse {

        val user = userService.getCurrentUser()
        val timeSlots = queryStudyRoomPort.queryTimeSlotsBySchoolId(user.schoolId)

        return QueryTimeSlotsResponse(
            timeSlots = timeSlots.map {
                QueryTimeSlotsResponse.TimeSlotElement(
                    id = it.id,
                    startTime = it.startTime,
                    endTime = it.endTime
                )
            }
        )
    }
}

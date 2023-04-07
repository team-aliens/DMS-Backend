package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.QueryTimeSlotsResponse
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class QueryTimeSlotsUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {
    fun execute(): QueryTimeSlotsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val currentUser = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val timeSlots = queryStudyRoomPort.queryTimeSlotsBySchoolId(currentUser.schoolId)

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

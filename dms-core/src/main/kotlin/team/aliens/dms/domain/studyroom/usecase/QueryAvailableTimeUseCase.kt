package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.QueryAvailableTimeResponse
import team.aliens.dms.domain.studyroom.exception.AvailableTimeNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryAvailableTimePort
import team.aliens.dms.domain.user.service.GetUserService

@ReadOnlyUseCase
class QueryAvailableTimeUseCase(
    private val getUserService: GetUserService,
    private val queryAvailableTimePort: QueryAvailableTimePort
) {

    fun execute(): QueryAvailableTimeResponse {

        val user = getUserService.getCurrentUser()

        val availableTime = queryAvailableTimePort
            .queryAvailableTimeBySchoolId(user.schoolId) ?: throw AvailableTimeNotFoundException

        return QueryAvailableTimeResponse(
            startAt = availableTime.startAt.withSecond(0),
            endAt = availableTime.endAt.withSecond(0)
        )
    }
}

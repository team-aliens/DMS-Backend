package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.QueryAvailableTimeResponse
import team.aliens.dms.domain.studyroom.exception.AvailableTimeNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryAvailableTimePort
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryAvailableTimeUseCase(
    private val userService: UserService,
    private val queryAvailableTimePort: QueryAvailableTimePort
) {

    fun execute(): QueryAvailableTimeResponse {

        val user = userService.getCurrentUser()

        val availableTime = queryAvailableTimePort
            .queryAvailableTimeBySchoolId(user.schoolId) ?: throw AvailableTimeNotFoundException

        return QueryAvailableTimeResponse(
            startAt = availableTime.startAt.withSecond(0),
            endAt = availableTime.endAt.withSecond(0)
        )
    }
}

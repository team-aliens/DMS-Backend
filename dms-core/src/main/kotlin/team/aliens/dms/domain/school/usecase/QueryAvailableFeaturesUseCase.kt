package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.dto.QueryAvailableFeaturesResponse
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryAvailableFeaturesUseCase(
    private val userService: UserService,
    private val querySchoolPort: QuerySchoolPort
) {

    fun execute(): QueryAvailableFeaturesResponse {
        val user = userService.getCurrentUser()
        val availableFeatures = querySchoolPort.queryAvailableFeaturesBySchoolId(user.schoolId)
            ?: throw FeatureNotFoundException

        return QueryAvailableFeaturesResponse(
            mealService = availableFeatures.mealService,
            noticeService = availableFeatures.noticeService,
            pointService = availableFeatures.pointService,
            studyRoomService = availableFeatures.studyRoomService,
            remainService = availableFeatures.remainService
        )
    }
}

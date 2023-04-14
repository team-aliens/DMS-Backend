package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.dto.QueryAvailableFeaturesResponse
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.user.service.GetUserService

@ReadOnlyUseCase
class QueryAvailableFeaturesUseCase(
    private val getUserService: GetUserService,
    private val querySchoolPort: QuerySchoolPort
) {

    fun execute(): QueryAvailableFeaturesResponse {
        val user = getUserService.getCurrentUser()
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

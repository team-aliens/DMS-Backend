package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.dto.QueryAvailableFeaturesResponse
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.school.spi.SchoolQueryUserPort
import team.aliens.dms.domain.school.spi.SchoolSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class QueryAvailableFeaturesUseCase(
    private val securityPort: SchoolSecurityPort,
    private val queryUserPort: SchoolQueryUserPort,
    private val querySchoolPort: QuerySchoolPort
) {

    fun execute(): QueryAvailableFeaturesResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

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

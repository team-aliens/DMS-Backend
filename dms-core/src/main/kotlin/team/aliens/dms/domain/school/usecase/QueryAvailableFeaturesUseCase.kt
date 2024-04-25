package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.dto.AvailableFeaturesResponse
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryAvailableFeaturesUseCase(
    private val userService: UserService,
    private val schoolService: SchoolService
) {

    fun execute(): AvailableFeaturesResponse {
        val user = userService.getCurrentUser()
        val availableFeatures = schoolService.getAvailableFeaturesBySchoolId(user.schoolId)

        return AvailableFeaturesResponse(
            mealService = availableFeatures.mealService,
            noticeService = availableFeatures.noticeService,
            pointService = availableFeatures.pointService,
            studyRoomService = availableFeatures.studyRoomService,
            remainService = availableFeatures.remainService,
            outingService = availableFeatures.outingService
        )
    }
}

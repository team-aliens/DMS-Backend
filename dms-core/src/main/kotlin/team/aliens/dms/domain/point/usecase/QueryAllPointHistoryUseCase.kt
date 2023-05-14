package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.dto.AllPointHistoryResponse
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryAllPointHistoryUseCase(
    private val userService: UserService,
    private val pointService: PointService
) {

    fun execute(type: PointRequestType, pageData: PageData): AllPointHistoryResponse {

        val user = userService.getCurrentUser()

        val pointHistories = pointService.queryPointHistoryBySchoolIdAndType(
            schoolId = user.schoolId,
            type = PointRequestType.toPointType(type),
            isCancel = false,
            pageData = pageData
        )

        return AllPointHistoryResponse(pointHistories = pointHistories)
    }
}

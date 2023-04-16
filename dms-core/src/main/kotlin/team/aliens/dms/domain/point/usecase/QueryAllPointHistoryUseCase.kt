package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryAllPointHistoryResponse
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryAllPointHistoryUseCase(
    private val userService: UserService,
    private val queryPointHistoryPort: QueryPointHistoryPort
) {

    fun execute(type: PointRequestType, pageData: PageData): QueryAllPointHistoryResponse {

        val user = userService.getCurrentUser()

        val pointHistories = queryPointHistoryPort.queryPointHistoryBySchoolIdAndType(
            schoolId = user.schoolId,
            type = PointRequestType.toPointType(type),
            isCancel = false,
            pageData = pageData
        )

        return QueryAllPointHistoryResponse(pointHistories)
    }
}

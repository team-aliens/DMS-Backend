package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryAllPointHistoryResponse
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.service.GetUserService
import team.aliens.dms.domain.user.spi.QueryUserPort

@ReadOnlyUseCase
class QueryAllPointHistoryUseCase(
    private val getUserService: GetUserService,
    private val queryPointHistoryPort: QueryPointHistoryPort
) {

    fun execute(type: PointRequestType, pageData: PageData): QueryAllPointHistoryResponse {

        val user = getUserService.getCurrentUser()

        val pointHistories = queryPointHistoryPort.queryPointHistoryBySchoolIdAndType(
            schoolId = user.schoolId,
            type = PointRequestType.toPointType(type),
            isCancel = false,
            pageData = pageData
        )

        return QueryAllPointHistoryResponse(pointHistories)
    }
}

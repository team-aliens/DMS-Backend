package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryAllPointHistoryResponse
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort

@ReadOnlyUseCase
class QueryAllPointHistoryUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryPointHistoryPort: QueryPointHistoryPort
) {

    fun execute(type: PointRequestType, pageData: PageData): QueryAllPointHistoryResponse {

        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val pointHistories = queryPointHistoryPort.queryPointHistoryBySchoolIdAndType(
            schoolId = manager.schoolId,
            type = PointRequestType.toPointType(type),
            isCancel = false,
            pageData = pageData
        )

        return QueryAllPointHistoryResponse(pointHistories)
    }
}

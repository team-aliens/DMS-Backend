package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.user.service.GetUserService

@ReadOnlyUseCase
class QueryPointHistoryUseCase(
    private val getUserService: GetUserService,
    private val queryPointHistoryPort: QueryPointHistoryPort
) {

    fun execute(type: PointRequestType, pageData: PageData): QueryPointHistoryResponse {

        val student = getUserService.getCurrentStudent()
        val gcn = student.gcn
        val name = student.name
        val pointType = PointRequestType.toPointType(type)

        val pointHistories =
            queryPointHistoryPort.queryPointHistoryByStudentGcnAndNameAndType(
                gcn = gcn,
                studentName = name,
                type = pointType,
                isCancel = false,
                pageData = pageData
            )

        val (bonusTotal, minusTotal) =
            queryPointHistoryPort.queryBonusAndMinusTotalPointByStudentGcnAndName(gcn, name)

        return QueryPointHistoryResponse(
            totalPoint = getTotalPoint(type, bonusTotal, minusTotal),
            points = pointHistories
        )
    }

    private fun getTotalPoint(
        type: PointRequestType,
        bonusTotal: Int,
        minusTotal: Int
    ): Int {
        val totalPoint = when (type) {
            PointRequestType.BONUS -> bonusTotal
            PointRequestType.MINUS -> minusTotal
            PointRequestType.ALL -> bonusTotal - minusTotal
        }
        return totalPoint
    }
}

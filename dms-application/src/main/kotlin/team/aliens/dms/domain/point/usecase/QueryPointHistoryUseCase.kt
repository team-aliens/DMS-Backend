package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.spi.PointQueryStudentPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException

@ReadOnlyUseCase
class QueryPointHistoryUseCase(
    private val securityPort: PointSecurityPort,
    private val queryStudentPort: PointQueryStudentPort,
    private val queryPointHistoryPort: QueryPointHistoryPort
) {

    fun execute(type: PointRequestType): QueryPointHistoryResponse {
        val currentStudentId = securityPort.getCurrentUserId()
        val currentStudent = queryStudentPort.queryStudentById(currentStudentId) ?: throw StudentNotFoundException

        val gcn = currentStudent.gcn
        val name = currentStudent.name
        val pointType = PointRequestType.toPointType(type)

        val pointHistories =
            queryPointHistoryPort.queryPointHistoryByStudentGcnAndNameAndType(
                gcn = gcn,
                studentName = name,
                type = pointType,
                isCancel = false
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
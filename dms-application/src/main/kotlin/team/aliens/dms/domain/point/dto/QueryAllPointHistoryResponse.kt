package team.aliens.dms.domain.point.dto

import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDate
import java.util.UUID

data class QueryAllPointHistoryResponse(
    val pointHistories: List<PointHistory>
) {
    data class PointHistory(
        val pointHistoryId: UUID,
        val studentName: String,
        val studentGcn: String,
        val date: LocalDate,
        val pointName: String,
        val pointType: PointType,
        val pointScore: Int
    )
}
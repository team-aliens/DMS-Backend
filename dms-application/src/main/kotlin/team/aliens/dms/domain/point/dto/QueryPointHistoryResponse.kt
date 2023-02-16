package team.aliens.dms.domain.point.dto

import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDate

data class QueryPointHistoryResponse(
    val totalPoint: Int,
    val points: List<Point>
) {
    data class Point(
        val date: LocalDate,
        val type: PointType,
        val name: String,
        val score: Int
    )
}

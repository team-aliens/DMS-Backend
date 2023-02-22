package team.aliens.dms.domain.point.dto

import team.aliens.dms.domain.point.model.PointType
import java.util.UUID

data class QueryPointOptionsResponse(
    val pointOptions: List<PointOptionResponse>
) {
    data class PointOptionResponse(
        val pointOptionId: UUID,
        val type: PointType,
        val score: Int,
        val name: String
    )
}

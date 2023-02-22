package team.aliens.dms.domain.point.dto

data class QueryPointHistoryResponse(
    val totalPoint: Int,
    val points: List<PointHistoryDto>
)

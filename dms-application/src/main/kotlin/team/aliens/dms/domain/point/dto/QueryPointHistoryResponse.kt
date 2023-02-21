package team.aliens.dms.domain.point.dto

import team.aliens.dms.domain.point.spi.vo.PointHistoryVO

data class QueryPointHistoryResponse(
    val totalPoint: Int,
    val points: List<PointHistoryVO>
)

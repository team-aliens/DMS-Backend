package team.aliens.dms.domain.point.dto

import team.aliens.dms.domain.point.spi.vo.PointHistoryVO

data class QueryStudentPointHistoryResponse(
    val pointHistories: List<PointHistoryVO>
)

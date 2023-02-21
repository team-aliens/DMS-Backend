package team.aliens.dms.domain.point.dto

import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.vo.PointHistoryVO
import java.time.LocalDate

data class QueryPointHistoryResponse(
    val totalPoint: Int,
    val points: List<PointHistoryVO>
)

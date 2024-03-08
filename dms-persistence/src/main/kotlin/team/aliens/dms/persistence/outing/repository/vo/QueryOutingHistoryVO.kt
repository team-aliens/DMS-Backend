package team.aliens.dms.persistence.outing.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.outing.spi.vo.OutingHistoryVO
import java.time.LocalTime
import java.util.UUID

class QueryOutingHistoryVO @QueryProjection constructor(
    outingApplicationId: UUID,
    name: String?,
    outingType: String,
    companionCount: Int,
    outingTime: LocalTime,
    arrivalTime: LocalTime
) : OutingHistoryVO(
    outingApplicationId = outingApplicationId,
    name = name,
    outingType = outingType,
    companionCount = companionCount,
    outingTime = outingTime,
    arrivalTime = arrivalTime
)

package team.aliens.dms.persistence.outing.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.spi.vo.OutingHistoryVO
import java.time.LocalTime
import java.util.UUID

class QueryOutingHistoryVO @QueryProjection constructor(
    outingApplicationId: UUID,
    studentName: String,
    outingType: String,
    outingCompanionCount: Int,
    outingStatus: OutingStatus,
    outingTime: LocalTime,
    arrivalTime: LocalTime
) : OutingHistoryVO(
    outingApplicationId = outingApplicationId,
    studentName = studentName,
    outingType = outingType,
    outingCompanionCount = outingCompanionCount,
    outingStatus = outingStatus,
    outingTime = outingTime,
    arrivalTime = arrivalTime
)

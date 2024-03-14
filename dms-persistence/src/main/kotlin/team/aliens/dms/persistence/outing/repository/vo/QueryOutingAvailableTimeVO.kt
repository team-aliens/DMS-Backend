package team.aliens.dms.persistence.outing.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.outing.spi.vo.OutingAvailableTimeVO
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

class QueryOutingAvailableTimeVO @QueryProjection constructor(
    outingAvailableTimeId: UUID,
    outingTime: LocalTime,
    arrivalTime: LocalTime,
    enabled: Boolean,
    dayOfWeek: DayOfWeek
) : OutingAvailableTimeVO(
    outingAvailableTimeId = outingAvailableTimeId,
    outingTime = outingTime,
    arrivalTime = arrivalTime,
    enabled = enabled,
    dayOfWeek = dayOfWeek
)

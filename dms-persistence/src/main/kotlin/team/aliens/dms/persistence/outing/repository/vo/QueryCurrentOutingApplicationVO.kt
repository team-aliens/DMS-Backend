package team.aliens.dms.persistence.outing.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.vo.CurrentOutingApplicationVO
import java.time.LocalDate
import java.time.LocalTime

class QueryCurrentOutingApplicationVO @QueryProjection constructor(
    outAt: LocalDate,
    outingType: String,
    status: OutingStatus,
    outingTime: LocalTime,
    arrivalTime: LocalTime,
    outingCompanions: List<String>
) : CurrentOutingApplicationVO(
    outAt = outAt,
    outingType = outingType,
    status = status,
    outingTime = outingTime,
    arrivalTime = arrivalTime,
    outingCompanions = outingCompanions
)

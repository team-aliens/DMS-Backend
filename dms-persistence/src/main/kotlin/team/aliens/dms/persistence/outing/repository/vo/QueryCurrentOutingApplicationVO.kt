package team.aliens.dms.persistence.outing.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.spi.vo.CurrentOutingApplicationVO
import java.time.LocalDate
import java.time.LocalTime

class QueryCurrentOutingApplicationVO @QueryProjection constructor(
    outingDate: LocalDate,
    outingTypeTitle: String,
    status: OutingStatus,
    outingTime: LocalTime,
    arrivalTime: LocalTime,
    reason: String?,
    outingCompanions: List<String>
) : CurrentOutingApplicationVO(
    outingDate = outingDate,
    outingTypeTitle = outingTypeTitle,
    status = status,
    outingTime = outingTime,
    arrivalTime = arrivalTime,
    reason = reason,
    outingCompanions = outingCompanions
)

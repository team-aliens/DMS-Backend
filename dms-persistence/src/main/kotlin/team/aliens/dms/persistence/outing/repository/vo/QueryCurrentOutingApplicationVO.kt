package team.aliens.dms.persistence.outing.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.outing.spi.vo.CurrentOutingApplicationVO
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class QueryCurrentOutingApplicationVO @QueryProjection constructor(
    id: UUID,
    outingDate: LocalDate,
    outingTypeTitle: String,
    outingTime: LocalTime,
    arrivalTime: LocalTime,
    reason: String?,
    outingApplicant: String,
    outingCompanions: List<String>
) : CurrentOutingApplicationVO(
    id = id,
    outingDate = outingDate,
    outingTypeTitle = outingTypeTitle,
    outingTime = outingTime,
    arrivalTime = arrivalTime,
    reason = reason,
    outingApplicant = outingApplicant,
    outingCompanions = outingCompanions
)

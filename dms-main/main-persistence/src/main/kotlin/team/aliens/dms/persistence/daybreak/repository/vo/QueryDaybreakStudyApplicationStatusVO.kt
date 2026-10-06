package team.aliens.dms.persistence.daybreak.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationStatusVO
import java.time.LocalDate

class QueryDaybreakStudyApplicationStatusVO @QueryProjection constructor(
    status: Status,
    dates: List<LocalDate>
) : DaybreakStudyApplicationStatusVO(
    status = status,
    dates = dates
)

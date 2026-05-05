package team.aliens.dms.domain.daybreak.spi.vo

import team.aliens.dms.domain.daybreak.model.Status
import java.time.LocalDate

open class DaybreakStudyApplicationStatusVO(
    val status: Status,
    val startDate: LocalDate,
    val endDate: LocalDate
)

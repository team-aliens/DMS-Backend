package team.aliens.dms.domain.outing.spi

import team.aliens.dms.domain.outing.model.OutingAvailableTime
import java.time.DayOfWeek
import java.util.UUID

interface QueryOutingAvailableTimePort {

    fun queryOutingAvailableTimeByDayOfWeek(dayOfWeek: DayOfWeek): OutingAvailableTime?

    fun queryOutingAvailableTimesByDayOfWeek(dayOfWeek: DayOfWeek): List<OutingAvailableTime>

    fun queryOutingAvailableTimeById(outingAvailableTimeId: UUID): OutingAvailableTime?
}

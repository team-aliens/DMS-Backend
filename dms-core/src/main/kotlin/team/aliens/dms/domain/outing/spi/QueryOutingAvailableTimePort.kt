package team.aliens.dms.domain.outing.spi

import team.aliens.dms.domain.outing.model.OutingAvailableTime
import java.time.DayOfWeek

interface QueryOutingAvailableTimePort {

    fun queryOutingAvailableTimeByDayOfWeek(dayOfWeek: DayOfWeek): OutingAvailableTime?

    fun queryOutingAvailableTimesByDayOfWeek(dayOfWeek: DayOfWeek): List<OutingAvailableTime>
}

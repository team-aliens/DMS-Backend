package team.aliens.dms.domain.outing.spi

import team.aliens.dms.domain.outing.model.OutingAvailableTime
import java.time.DayOfWeek
import java.util.UUID

interface QueryOutingAvailableTimePort {

    fun queryOutingAvailableTimeByDayOfWeekAndSchoolId(dayOfWeek: DayOfWeek, schoolId: UUID): OutingAvailableTime?

    fun queryOutingAvailableTimesByDayOfWeekAndSchoolId(dayOfWeek: DayOfWeek, schoolId: UUID): List<OutingAvailableTime>

    fun queryOutingAvailableTimeById(outingAvailableTimeId: UUID): OutingAvailableTime?
}

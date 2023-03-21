package team.aliens.dms.domain.remain.stub

import team.aliens.dms.domain.remain.model.RemainAvailableTime
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

internal fun createRemainAvailableTimeStub(
    id: UUID = UUID.randomUUID(),
    startDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    startTime: LocalTime = LocalTime.MIN,
    endDayOfWeek: DayOfWeek = DayOfWeek.SUNDAY,
    endTime: LocalTime = LocalTime.MAX
) = RemainAvailableTime(
    id = id,
    startDayOfWeek = startDayOfWeek,
    startTime = startTime,
    endDayOfWeek = endDayOfWeek,
    endTime
)

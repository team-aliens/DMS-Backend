package team.aliens.dms.domain.remain.stub

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID
import team.aliens.dms.domain.remain.model.RemainAvailableTime
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.model.RemainStatus

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

internal fun createRemainStatusStub(
    id: UUID = UUID.randomUUID(),
    remainOptionId: UUID = UUID.randomUUID(),
    createdAt: LocalDateTime = LocalDateTime.now()
) = RemainStatus(
    id = id,
    remainOptionId = remainOptionId,
    createdAt = createdAt
)

internal fun createRemainOptionStub(
    id: UUID = UUID.randomUUID(),
    schoolId: UUID = UUID.randomUUID(),
    title: String = "title",
    description: String = "description"
) = RemainOption(
    id = id,
    schoolId = schoolId,
    title = title,
    description = description
)
package team.aliens.dms.domain.outing.stub

import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingAvailableTime
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

internal fun createOutingApplicationStub(
    id: UUID = UUID.randomUUID(),
    studentId: UUID = UUID.randomUUID(),
    createdAt: LocalDateTime = LocalDateTime.now(),
    outingDate: LocalDate = LocalDate.now(),
    outingTime: LocalTime = LocalTime.now(),
    arrivalTime: LocalTime = LocalTime.now(),
    isApproved: Boolean = false,
    isReturned: Boolean = false,
    reason: String? = null,
    outingTypeTitle: String = "식사 외출",
    schoolId: UUID = UUID.randomUUID(),
    companionIds: List<UUID>? = null
) = OutingApplication(
    id = id,
    studentId = studentId,
    createdAt = createdAt,
    outingDate = outingDate,
    outingTime = outingTime,
    arrivalTime = arrivalTime,
    isApproved = isApproved,
    isReturned = isReturned,
    reason = reason,
    outingTypeTitle = outingTypeTitle,
    schoolId = schoolId
)

internal fun createOutingAvailableTimeStub(
    id: UUID = UUID.randomUUID(),
    schoolId: UUID = UUID.randomUUID(),
    outingTime: LocalTime = LocalTime.now(),
    arrivalTime: LocalTime = LocalTime.now(),
    enabled: Boolean = true,
    dayOfWeek: DayOfWeek = DayOfWeek.SUNDAY
) = OutingAvailableTime(
    id = id,
    schoolId = schoolId,
    outingTime = outingTime,
    arrivalTime = arrivalTime,
    enabled = enabled,
    dayOfWeek = dayOfWeek
)

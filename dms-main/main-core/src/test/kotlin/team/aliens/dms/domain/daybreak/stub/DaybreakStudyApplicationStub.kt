package team.aliens.dms.domain.daybreak.stub

import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.Status
import java.time.LocalDate
import java.util.UUID

internal fun createDaybreakStudyApplicationStub(
    id: UUID = UUID.randomUUID(),
    studyTypeId: UUID = UUID.randomUUID(),
    startDate: LocalDate = LocalDate.now(),
    endDate: LocalDate = LocalDate.now().plusDays(3),
    reason: String = "새벽 자습 신청합니다.",
    status: Status = Status.PENDING,
    previousStatus: Status? = null,
    teacherId: UUID = UUID.randomUUID(),
    studentId: UUID = UUID.randomUUID(),
    schoolId: UUID = UUID.randomUUID()
): DaybreakStudyApplication {
    return DaybreakStudyApplication(
        id = id,
        studyTypeId = studyTypeId,
        startDate = startDate,
        endDate = endDate,
        reason = reason,
        status = status,
        previousStatus = previousStatus,
        teacherId = teacherId,
        studentId = studentId,
        schoolId = schoolId
    )
}

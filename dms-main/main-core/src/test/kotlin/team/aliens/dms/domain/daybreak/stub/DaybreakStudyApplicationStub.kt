package team.aliens.dms.domain.daybreak.stub

import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.Status
import java.time.LocalDate
import java.util.UUID

internal fun createDaybreakStudyApplicationStub(
    id: UUID = UUID.randomUUID(),
    studyTypeId: UUID = UUID.randomUUID(),
    dates: List<LocalDate> = listOf(LocalDate.now(), LocalDate.now().plusDays(3)),
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
        dates = dates,
        reason = reason,
        status = status,
        previousStatus = previousStatus,
        teacherId = teacherId,
        studentId = studentId,
        schoolId = schoolId
    )
}

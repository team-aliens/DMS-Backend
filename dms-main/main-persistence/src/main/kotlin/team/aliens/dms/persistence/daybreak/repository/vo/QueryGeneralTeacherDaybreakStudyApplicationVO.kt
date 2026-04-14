package team.aliens.dms.persistence.daybreak.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.spi.vo.GeneralTeacherDaybreakStudyApplicationVO
import team.aliens.dms.domain.student.model.Student
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class QueryGeneralTeacherDaybreakStudyApplicationVO @QueryProjection constructor(
    applicationId: UUID,
    typeName: String,
    createdAt: LocalDateTime,
    startDate: LocalDate,
    endDate: LocalDate,
    status: Status,
    reason: String,
    studentName: String,
    studentGrade: Int,
    studentClassRoom: Int,
    studentNumber: Int,
) : GeneralTeacherDaybreakStudyApplicationVO(
    applicationId = applicationId,
    typeName = typeName,
    createdAt = createdAt,
    startDate = startDate,
    endDate = endDate,
    status = status,
    reason = reason,
    studentName = studentName,
    studentGcn = Student.processGcn(studentGrade, studentClassRoom, studentNumber)
)

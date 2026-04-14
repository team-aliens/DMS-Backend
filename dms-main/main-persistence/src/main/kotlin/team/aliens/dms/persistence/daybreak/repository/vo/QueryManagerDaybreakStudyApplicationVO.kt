package team.aliens.dms.persistence.daybreak.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.daybreak.spi.vo.ManagerDaybreakStudyApplicationVO
import team.aliens.dms.domain.student.model.Student
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

open class QueryManagerDaybreakStudyApplicationVO @QueryProjection constructor(
    applicationId: UUID,
    typeName: String,
    teacherName: String,
    createdAt: LocalDateTime,
    startDate: LocalDate,
    endDate: LocalDate,
    reason: String,
    studentName: String,
    studentGrade: Int,
    studentClassRoom: Int,
    studentNumber: Int,
) : ManagerDaybreakStudyApplicationVO(
    applicationId = applicationId,
    typeName = typeName,
    teacherName = teacherName,
    createdAt = createdAt,
    startDate = startDate,
    endDate = endDate,
    reason = reason,
    studentName = studentName,
    studentGcn = Student.processGcn(studentGrade, studentClassRoom, studentNumber)
)

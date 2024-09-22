package team.aliens.dms.persistence.outing.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.outing.spi.vo.OutingApplicationVO
import team.aliens.dms.domain.outing.spi.vo.OutingCompanionVO
import java.time.LocalDate
import java.time.LocalTime

class QueryOutingApplicationVO @QueryProjection constructor(
    studentName: String,
    studentGrade: Int,
    studentClassRoom: Int,
    studentNumber: Int,
    reason: String?,
    outingType: String?,
    outingDate: LocalDate,
    outingTime: LocalTime,
    arrivalTime: LocalTime,
    outingCompanionVOs: List<OutingCompanionVO>
) : OutingApplicationVO(
    studentName = studentName,
    studentGrade = studentGrade,
    studentClassRoom = studentClassRoom,
    studentNumber = studentNumber,
    reason = reason,
    outingType = outingType,
    outingDate = outingDate,
    outingTime = outingTime,
    arrivalTime = arrivalTime,
    outingCompanionVOs = outingCompanionVOs
)

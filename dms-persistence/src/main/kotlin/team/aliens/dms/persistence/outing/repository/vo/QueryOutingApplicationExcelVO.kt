package team.aliens.dms.persistence.outing.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.outing.spi.vo.OutingApplicationExcelVO
import team.aliens.dms.domain.outing.spi.vo.OutingCompanionVO
import java.time.LocalDate
import java.time.LocalTime

class QueryOutingApplicationExcelVO @QueryProjection constructor(
    studentName: String,
    studentGrade: Int,
    studentClassRoom: Int,
    studentNumber: Int,
    outingType: String,
    reason: String,
    outingDate: LocalDate,
    outingTime: LocalTime,
    arrivalTime: LocalTime,
    outingCompanionVOs: List<OutingCompanionVO>
) : OutingApplicationExcelVO(
    studentName = studentName,
    studentGrade = studentGrade,
    studentClassRoom = studentClassRoom,
    studentNumber = studentNumber,
    outingType = outingType,
    reason = reason,
    outingDate = outingDate,
    outingTime = outingTime,
    arrivalTime = arrivalTime,
    outingCompanionVOs = outingCompanionVOs
)

package team.aliens.dms.persistence.outing.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.outing.spi.vo.OutingApplicationVO
import team.aliens.dms.domain.outing.spi.vo.OutingCompanionVO
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class QueryOutingApplicationVO @QueryProjection constructor(
    id: UUID,
    studentName: String,
    studentGrade: Int,
    studentClassRoom: Int,
    studentNumber: Int,
    outingType: String,
    outingDate: LocalDate,
    outingTime: LocalTime,
    arrivalTime: LocalTime,
    isApproved: Boolean,
    isComeback: Boolean,
    outingCompanionVOs: List<OutingCompanionVO>
) : OutingApplicationVO(
    id = id,
    studentName = studentName,
    studentGrade = studentGrade,
    studentClassRoom = studentClassRoom,
    studentNumber = studentNumber,
    outingType = outingType,
    outingDate = outingDate,
    outingTime = outingTime,
    arrivalTime = arrivalTime,
    isApproved = isApproved,
    isComeback = isComeback,
    outingCompanionVOs = outingCompanionVOs
)

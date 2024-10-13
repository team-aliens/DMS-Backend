package team.aliens.dms.persistence.outing.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.outing.spi.vo.OutingHistoryVO
import java.time.LocalTime
import java.util.UUID

class QueryOutingHistoryVO @QueryProjection constructor(
    id: UUID,
    studentGcn: String,
    studentName: String,
    outingType: String,
    outingTime: LocalTime,
    arrivalTime: LocalTime,
    isApproved: Boolean,
    isComeback: Boolean
) : OutingHistoryVO(
    id = id,
    studentGcn = studentGcn,
    studentName = studentName,
    outingType = outingType,
    outingTime = outingTime,
    arrivalTime = arrivalTime,
    isApproved = isApproved,
    isComeback = isComeback
)

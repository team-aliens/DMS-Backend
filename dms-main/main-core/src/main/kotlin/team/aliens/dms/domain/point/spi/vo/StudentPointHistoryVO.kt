package team.aliens.dms.domain.point.spi.vo

import team.aliens.dms.domain.point.dto.StudentPointHistoryResponse
import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDate
import java.util.UUID

open class StudentPointHistoryVO(
    pointHistoryId: UUID,
    studentName: String,
    studentGcn: String,
    date: LocalDate,
    pointName: String,
    pointType: PointType,
    pointScore: Int
) : StudentPointHistoryResponse(
    pointHistoryId = pointHistoryId,
    studentName = studentName,
    studentGcn = studentGcn,
    date = date,
    pointName = pointName,
    pointType = pointType,
    pointScore = pointScore
)

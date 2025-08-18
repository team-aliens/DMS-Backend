package team.aliens.dms.persistence.point.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.point.spi.vo.StudentTotalVO
import java.util.UUID

class QueryStudentTotalVO @QueryProjection constructor(
    id: UUID,
    bonusTotal: Int,
    minusTotal: Int
) : StudentTotalVO(
    studentId = id,
    bonusTotal = bonusTotal,
    minusTotal = minusTotal
)

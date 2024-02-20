package team.aliens.dms.persistence.outing.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.outing.spi.vo.OutingCompanionVO

class QueryOutingCompanionVO @QueryProjection constructor(
    studentName: String?,
    studentGrade: Int?,
    studentClassRoom: Int?,
    studentNumber: Int?
) : OutingCompanionVO(
    studentName = studentName ?: "",
    studentGrade = studentGrade ?: 0,
    studentClassRoom = studentClassRoom ?: 0,
    studentNumber = studentNumber ?: 0
)

package team.aliens.dms.persistence.outing.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.outing.spi.vo.OutingCompanionDetailsVO
import java.util.UUID

class QueryOutingCompanionDetailsVO @QueryProjection constructor(
    id: UUID,
    name: String,
    grade: Int,
    classRoom: Int,
    number: Int,
    roomNumber: String,
) : OutingCompanionDetailsVO(
    id = id,
    studentName = name,
    studentGrade = grade,
    studentClassRoom = classRoom,
    studentNumber = number,
    roomNumber = roomNumber
)

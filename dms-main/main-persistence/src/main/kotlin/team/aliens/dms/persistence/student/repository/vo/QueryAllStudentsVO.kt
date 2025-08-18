package team.aliens.dms.persistence.student.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.student.spi.vo.AllStudentsVO
import java.util.UUID

class QueryAllStudentsVO @QueryProjection constructor(
    id: UUID,
    name: String,
    grade: Int,
    classRoom: Int,
    number: Int,
    profileImageUrl: String
) : AllStudentsVO(
    id = id,
    name = name,
    grade = grade,
    classRoom = classRoom,
    number = number,
    profileImageUrl = profileImageUrl
)

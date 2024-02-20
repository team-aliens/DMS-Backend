package team.aliens.dms.persistence.student.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.student.spi.vo.AllStudentsVO

class QueryAllStudentsVO @QueryProjection constructor(
    name: String,
    grade: Int,
    classRoom: Int,
    number: Int,
    profileImageUrl: String
) : AllStudentsVO(
    name = name,
    grade = grade,
    classRoom = classRoom,
    number = number,
    profileImageUrl = profileImageUrl
)

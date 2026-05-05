package team.aliens.dms.persistence.teacher.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.teacher.spi.vo.TeacherVO
import java.util.UUID

class QueryTeacherVO @QueryProjection constructor(
    id: UUID,
    name: String
) : TeacherVO(
    id = id,
    name = name
)

package team.aliens.dms.persistence.tag.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.tag.spi.vo.StudentTagDetailVO
import java.util.UUID

class QueryStudentTagDetailVO @QueryProjection constructor(
    studentId: UUID,
    studentName: String,
    tagId: UUID,
    tagColor: String,
    tagName: String
) :StudentTagDetailVO(
    studentId = studentId,
    studentName = studentName,
    tagId = tagId,
    tagColor = tagColor,
    tagName = tagName
)
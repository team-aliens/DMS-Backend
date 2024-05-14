package team.aliens.dms.domain.tag.spi.vo

import java.util.UUID

open class StudentTagDetailVO(
    val studentId: UUID,
    val studentName: String,
    val tagId: UUID,
    val tagColor: String,
    val tagName: String
)

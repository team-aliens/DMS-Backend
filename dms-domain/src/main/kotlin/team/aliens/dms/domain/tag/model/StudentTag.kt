package team.aliens.dms.domain.tag.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class StudentTag(

    val studentId: UUID,

    val tagId: UUID,

    val createdAt: LocalDateTime

)

package team.aliens.dms.domain.student.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class StudentTag(

    val id: UUID,

    val studentId: UUID,

    val tagId: UUID,

    val createdAt: LocalDateTime

)

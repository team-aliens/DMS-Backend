package team.aliens.dms.domain.point.model

import team.aliens.dms.global.annotation.Aggregate
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class PointHistory(

    val id: UUID,

    val pointOptionId: UUID,

    val studentId: UUID,

    val createdAt: LocalDateTime
)
package team.aliens.dms.domain.point.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class PointHistory(

    val id: UUID = UUID(0, 0),

    val studentName: String,

    val gcn: String,

    val name: String,

    val score: Int,

    val type: PointType,

    val schoolId: UUID,

    val createdAt: LocalDateTime

)
package team.aliens.dms.domain.point.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class PointHistory(

    val id: UUID = UUID(0, 0),

    val studentName: String,

    val studentGcn: String,

    val bonusTotal: Int,

    val minusTotal: Int,

    val isCancel: Boolean,

    val pointName: String,

    val pointScore: Int,

    val pointType: PointType,

    val createdAt: LocalDateTime,

    val schoolId: UUID,

)
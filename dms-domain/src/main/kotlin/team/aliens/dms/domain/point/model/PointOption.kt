package team.aliens.dms.domain.point.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class PointOption(

    val id: UUID = UUID(0, 0),

    val schoolId: UUID,

    val name: String,

    val score: Int,

    val type: PointType

)
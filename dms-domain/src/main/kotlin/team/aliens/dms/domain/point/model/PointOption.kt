package team.aliens.dms.domain.point.model

import team.aliens.dms.global.annotation.Aggregate
import java.util.UUID

@Aggregate
data class PointOption(

    val id: UUID,

    val name: String,

    val score: Int
)
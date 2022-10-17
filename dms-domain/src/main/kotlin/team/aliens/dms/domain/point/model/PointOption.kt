package team.aliens.dms.domain.point.model

import team.aliens.dms.global.annotation.Aggregate
import java.util.UUID

@Aggregate
data class PointOption(

    val id: UUID = UUID(0, 0),

    val name: String,

    val score: Int
)
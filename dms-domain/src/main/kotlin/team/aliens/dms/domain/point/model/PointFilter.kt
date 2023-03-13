package team.aliens.dms.domain.point.model

import java.util.UUID

class PointFilter(

    val id: UUID = UUID(0, 0),

    val schoolId: UUID,

    val name: String,

    val pointType: PointType,

    val maxPoint: Int,

    val minPoint: Int

)
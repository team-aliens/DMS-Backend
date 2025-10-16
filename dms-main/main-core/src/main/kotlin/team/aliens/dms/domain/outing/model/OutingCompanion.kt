package team.aliens.dms.domain.outing.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class OutingCompanion(

    val outingApplicationId: UUID,

    val studentId: UUID

)

package team.aliens.dms.domain.feature

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class AvailableService(

    val schoolId: UUID,

    val mealService: Boolean,

    val noticeService: Boolean,

    val pointService: Boolean

)
package team.aliens.dms.domain.school.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class AvailableFeature(

    val schoolId: UUID,

    val mealService: Boolean,

    val noticeService: Boolean,

    val pointService: Boolean,

    val studyRoomService: Boolean,

    val remainService: Boolean,

    val outingService: Boolean,

    val volunteerService: Boolean

)

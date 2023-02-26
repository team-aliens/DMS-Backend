package team.aliens.dms.domain.studyroom.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalTime
import java.util.UUID

@Aggregate
data class AvailableTime(

    val schoolId: UUID,

    val startAt: LocalTime,

    val endAt: LocalTime

)

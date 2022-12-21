package team.aliens.dms.domain.studyroom.model

import java.time.LocalTime
import java.util.UUID
import team.aliens.dms.common.annotation.Aggregate

@Aggregate
data class AvailableTime(

    val schoolId: UUID,

    val startAt: LocalTime,

    val endAt: LocalTime

)
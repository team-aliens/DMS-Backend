package team.aliens.dms.domain.outing.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import java.time.LocalTime
import java.util.*

@Aggregate
class OutingAvailableTime(

    val id: UUID = UUID(0, 0),

    override val schoolId: UUID,

    val outingTime: LocalTime,

    val arrivalTime: LocalTime,

    val enabled: Boolean,

    val dayOfWeek: String

) : SchoolIdDomain

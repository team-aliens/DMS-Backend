package team.aliens.dms.domain.volunteer.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class VolunteerScore(

    val id: UUID = UUID(0, 0),

    val applicationId: UUID,

    val assignScore: Int,
)

package team.aliens.dms.domain.volunteer.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class VolunteerApplication(

    val id: UUID = UUID(0, 0),

    val studentId: UUID,

    val volunteerId: UUID

)

package team.aliens.dms.domain.studyroom.model

import team.aliens.dms.common.model.SchoolIdDomain
import java.util.UUID

data class SeatType(

    val id: UUID = UUID(0, 0),

    override val schoolId: UUID,

    val name: String,

    val color: String

) : SchoolIdDomain

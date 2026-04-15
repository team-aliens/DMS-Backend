package team.aliens.dms.domain.daybreak.model

import team.aliens.dms.common.model.SchoolIdDomain
import java.util.UUID

data class DaybreakStudyType(

    val id: UUID = UUID(0, 0),

    val name: String,

    override val schoolId: UUID

): SchoolIdDomain

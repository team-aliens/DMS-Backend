package team.aliens.dms.domain.outing.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import java.util.UUID

@Aggregate
data class OutingType(

    val outingTitle: String,

    override val schoolId: UUID

) : SchoolIdDomain

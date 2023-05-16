package team.aliens.dms.domain.remain.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import java.util.UUID

@Aggregate
data class RemainOption(

    val id: UUID = UUID(0, 0),

    override val schoolId: UUID,

    val title: String,

    val description: String

) : SchoolIdDomain

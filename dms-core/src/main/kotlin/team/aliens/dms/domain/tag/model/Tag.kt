package team.aliens.dms.domain.tag.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import java.util.UUID

@Aggregate
data class Tag(

    val id: UUID = UUID(0, 0),

    val name: String,

    val color: String,

    override val schoolId: UUID

) : SchoolIdDomain

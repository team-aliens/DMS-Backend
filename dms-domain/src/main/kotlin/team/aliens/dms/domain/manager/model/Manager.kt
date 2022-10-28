package team.aliens.dms.domain.manager.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Manager(

    val managerId: UUID

)
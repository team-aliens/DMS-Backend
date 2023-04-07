package team.aliens.dms.domain.remain.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class RemainOption(

    val id: UUID = UUID(0, 0),

    val schoolId: UUID,

    val title: String,

    val description: String

)

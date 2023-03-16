package team.aliens.dms.domain.student.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Tag(

    val id: UUID = UUID(0, 0),

    val name: String,

    val schoolId: UUID

)

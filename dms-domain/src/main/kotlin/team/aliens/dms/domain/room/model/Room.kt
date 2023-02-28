package team.aliens.dms.domain.room.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Room(

    val id: UUID = UUID(0, 0),

    val number: String,

    val schoolId: UUID

)

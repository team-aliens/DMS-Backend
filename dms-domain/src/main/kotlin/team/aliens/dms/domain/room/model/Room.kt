package team.aliens.dms.domain.room.model

import team.aliens.dms.global.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Room(

    val roomNumber: Int,

    val schoolId: UUID
)
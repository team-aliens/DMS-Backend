package team.aliens.dms.domain.studyroom.model

import java.util.UUID

data class SeatType(

    val id: UUID = UUID(0, 0),

    val name: String,

    val color: String

)

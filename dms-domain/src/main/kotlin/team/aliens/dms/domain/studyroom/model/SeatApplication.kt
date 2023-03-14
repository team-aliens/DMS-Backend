package team.aliens.dms.domain.studyroom.model

import java.util.UUID

data class SeatApplication(

    val id: UUID = UUID.randomUUID(),

    val seatId: UUID,

    val timeSlotId: UUID?,

    val studentId: UUID
)

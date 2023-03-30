package team.aliens.dms.domain.studyroom.model

import java.util.UUID

data class SeatApplication(

    val seatId: UUID,

    val timeSlotId: UUID,

    val studentId: UUID
)

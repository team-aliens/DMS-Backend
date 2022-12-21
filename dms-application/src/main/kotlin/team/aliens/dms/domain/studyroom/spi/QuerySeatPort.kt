package team.aliens.dms.domain.studyroom.spi

import java.util.UUID
import team.aliens.dms.domain.studyroom.model.Seat

interface QuerySeatPort {

    fun querySeatById(seatId: UUID): Seat?

}
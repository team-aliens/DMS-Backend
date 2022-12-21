package team.aliens.dms.domain.studyroom.spi

import java.util.UUID
import team.aliens.dms.domain.studyroom.model.SeatType

interface QuerySeatTypePort {

    fun queryAllSeatTypeByUserId(userId: UUID): List<SeatType>

}
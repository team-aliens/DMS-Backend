package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.studyroom.model.SeatType

interface CommandSeatTypePort {

    fun saveSeatType(seatType: SeatType): SeatType

}
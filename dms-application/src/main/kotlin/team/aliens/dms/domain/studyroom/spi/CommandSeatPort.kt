package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.studyroom.model.Seat

interface CommandSeatPort {

    fun saveSeat(seat: Seat): Seat

}
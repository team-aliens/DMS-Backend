package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.studyroom.model.Seat

interface CommandStudyRoomPort {

    fun saveSeat(seat: Seat): Seat

    fun deleteSeat(seat: Seat)

}
package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.studyroom.model.Seat

interface ManagerCommandStudyRoomPort {

    fun saveSeat(seat: Seat): Seat
}

package team.aliens.dms.domain.studyroom.spi

import java.util.UUID

interface SeatTypeQueryStudyRoomPort {

    fun existsStudyRoomBySeatTypeId(seatTypeId: UUID): Boolean

}
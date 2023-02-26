package team.aliens.dms.domain.studyroom.spi

import java.util.UUID

interface SeatTypeQueryStudyRoomPort {

    fun existsSeatBySeatTypeId(seatTypeId: UUID): Boolean
}

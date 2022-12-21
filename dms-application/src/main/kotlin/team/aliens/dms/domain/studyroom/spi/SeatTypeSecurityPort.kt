package team.aliens.dms.domain.studyroom.spi

import java.util.UUID

interface SeatTypeSecurityPort {

    fun getCurrentUserId(): UUID

}
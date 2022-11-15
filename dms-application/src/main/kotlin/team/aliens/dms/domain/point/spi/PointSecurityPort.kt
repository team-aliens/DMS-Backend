package team.aliens.dms.domain.point.spi

import java.util.UUID

interface PointSecurityPort {

    fun getCurrentUserId(): UUID

}

package team.aliens.dms.domain.remain.spi

import java.util.UUID

interface RemainSecurityPort {

    fun getCurrentUserId(): UUID
}
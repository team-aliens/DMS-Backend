package team.aliens.dms.domain.manager.spi

import java.util.UUID

interface ManagerSecurityPort {

    fun getCurrentUserId(): UUID

    fun encodePassword(password: String): String
}

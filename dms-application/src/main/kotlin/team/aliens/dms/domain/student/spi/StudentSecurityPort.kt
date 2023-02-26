package team.aliens.dms.domain.student.spi

import java.util.UUID

interface StudentSecurityPort {

    fun encodePassword(password: String): String

    fun getCurrentUserId(): UUID
}

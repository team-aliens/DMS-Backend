package team.aliens.dms.domain.user.spi

import java.util.UUID

interface UserSecurityPort {

    fun getCurrentUserId(): UUID

    fun isPasswordMatch(rawPassword: String, encodedPassword: String): Boolean

    fun encodePassword(password: String): String
}

package team.aliens.dms.common.spi

import java.util.UUID

interface SecurityPort {

    fun isPasswordMatch(rawPassword: String, encodedPassword: String): Boolean

    fun getCurrentUserId(): UUID

    fun encodePassword(password: String): String
}

package team.aliens.dms.domain.auth.spi

interface AuthSecurityPort {

    fun isPasswordMatch(rawPassword: String, encodedPassword: String): Boolean

}
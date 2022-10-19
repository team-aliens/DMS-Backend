package team.aliens.dms.domain.user.spi

interface UserSecurityPort {

    fun encode(password: String): String
}
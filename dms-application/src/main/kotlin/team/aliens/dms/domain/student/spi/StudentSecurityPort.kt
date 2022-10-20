package team.aliens.dms.domain.student.spi

interface StudentSecurityPort {
    fun encode(password: String): String
}
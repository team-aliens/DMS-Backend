package team.aliens.dms.domain.student.spi

interface StudentSecurityPort {

    fun encodePassword(password: String): String

}
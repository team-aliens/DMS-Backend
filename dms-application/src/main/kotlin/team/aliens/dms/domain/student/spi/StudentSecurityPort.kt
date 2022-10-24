package team.aliens.dms.domain.student.spi

import java.util.*

interface StudentSecurityPort {
    fun getCurrentUserId(): UUID
    fun encode(password: String): String
}
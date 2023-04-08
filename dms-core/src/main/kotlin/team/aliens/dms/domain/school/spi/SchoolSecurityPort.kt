package team.aliens.dms.domain.school.spi

import java.util.UUID

interface SchoolSecurityPort {

    fun getCurrentUserId(): UUID
}

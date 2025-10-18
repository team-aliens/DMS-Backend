package team.aliens.dms.common.spi

import java.util.UUID

interface SecurityPort {

    fun getCurrentUserId(): UUID

    fun getCurrentUserSchoolId(): UUID
}

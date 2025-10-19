package team.aliens.dms.common.service.security

import java.util.UUID

interface SecurityService {

    fun getCurrentUserId(): UUID

    fun getCurrentSchoolId(): UUID
}

package team.aliens.dms.common.service.security

import team.aliens.dms.domain.auth.model.Authority
import java.util.UUID

interface SecurityService {

    fun encodePassword(password: String): String

    fun getCurrentUserId(): UUID

    fun checkIsPasswordMatches(rawPassword: String, encodedPassword: String)

    fun getCurrentSchoolId(): UUID

    fun getCurrentUserAuthority(): Authority
}

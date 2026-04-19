package team.aliens.dms.common.spi

import team.aliens.dms.domain.auth.model.Authority
import java.util.UUID

interface SecurityPort {

    fun isPasswordMatch(rawPassword: String, encodedPassword: String): Boolean

    fun isAuthenticated(): Boolean

    fun getCurrentUserId(): UUID

    fun getCurrentUserSchoolId(): UUID

    fun encodePassword(password: String): String

    fun isStudent(): Boolean

    fun getCurrentUserAuthority(): Authority
}

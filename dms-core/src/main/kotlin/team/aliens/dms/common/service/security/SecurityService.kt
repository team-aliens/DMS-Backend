package team.aliens.dms.common.service.security

import java.util.UUID

interface SecurityService {

    fun encodePassword(password: String): String

    fun getCurrentUserId(): UUID

    fun checkIsPasswordMatches(rawPassword: String, encodedPassword: String)
}

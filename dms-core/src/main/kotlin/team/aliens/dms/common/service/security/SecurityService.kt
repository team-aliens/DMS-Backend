package team.aliens.dms.common.service.security

interface SecurityService {
    fun encodePassword(password: String): String
    fun checkIsPasswordMatches(rawPassword: String, encodedPassword: String)
}

package team.aliens.dms.common.service

interface SecurityService {
    fun encodePassword(password: String): String
    fun checkIsPasswordMatches(rawPassword: String, encodedPassword: String)
}

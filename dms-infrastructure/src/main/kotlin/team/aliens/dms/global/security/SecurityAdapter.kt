package team.aliens.dms.global.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.spi.SecurityPort
import java.util.*

@Component
class SecurityAdapter(
    private val passwordEncoder: BCryptPasswordEncoder
) : SecurityPort {

    override fun getCurrentUserId(): UUID {
        return UUID.fromString(SecurityContextHolder.getContext().authentication.name)
    }

    override fun isPasswordMatch(rawPassword: String, encodedPassword: String) =
        passwordEncoder.matches(rawPassword, encodedPassword)

    override fun encode(password: String): String = passwordEncoder.encode(password)
}
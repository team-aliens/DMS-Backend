package team.aliens.dms.global.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.spi.SecurityPort
import java.util.UUID

@Component
class SecurityAdapter(
    private val passwordEncoder: PasswordEncoder
) : SecurityPort {

    override fun getCurrentUserId() : UUID {
        return UUID.fromString(SecurityContextHolder.getContext().authentication.name);
    }

    override fun encode(password: String): String = passwordEncoder.encode(password)
}
package team.aliens.dms.global.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.spi.SecurityPort
import java.util.UUID

@Component
class SecurityFacadeAdapter : SecurityPort {

    override fun getCurrentUserId() : UUID {
        return UUID.fromString(SecurityContextHolder.getContext().authentication.name);
    }
}
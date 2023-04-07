package team.aliens.dms.global.security.principle

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import team.aliens.dms.domain.manager.spi.QueryManagerPort
import team.aliens.dms.global.security.exception.InvalidTokenException
import java.util.UUID

@Component
class ManagerDetailsService(
    private val queryManagerPort: QueryManagerPort
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val manager = queryManagerPort.queryManagerById(UUID.fromString(username)) ?: throw InvalidTokenException

        return ManagerDetails(manager.id)
    }
}

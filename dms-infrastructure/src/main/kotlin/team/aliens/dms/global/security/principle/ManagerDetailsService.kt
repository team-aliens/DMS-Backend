package team.aliens.dms.global.security.principle

import java.util.UUID
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import team.aliens.dms.domain.manager.spi.QueryManagerPort
import team.aliens.dms.global.security.exception.InvalidTokenException

@Component
class ManagerDetailsService(
    private val queryManagerPort: QueryManagerPort
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val managerId = UUID.fromString(username)
        if (!queryManagerPort.existsManagerById(managerId)) {
            throw InvalidTokenException
        }
        return ManagerDetails(managerId)
    }
}

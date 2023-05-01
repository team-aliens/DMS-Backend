package team.aliens.dms.global.security.principle

import java.util.UUID
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.user.spi.QueryUserPort
import team.aliens.dms.global.security.exception.InvalidTokenException

@Component
class ManagerDetailsService(
    private val queryUserPort: QueryUserPort
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        queryUserPort.queryUserById(UUID.fromString(username)).apply {
            if (this == null || authority != Authority.MANAGER) {
                throw InvalidTokenException
            }
            return ManagerDetails(
                userId = id,
                schoolId = schoolId
            )
        }
    }
}

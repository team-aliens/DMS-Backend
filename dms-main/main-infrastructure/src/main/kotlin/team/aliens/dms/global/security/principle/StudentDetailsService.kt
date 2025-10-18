package team.aliens.dms.global.security.principle

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.user.spi.QueryUserPort
import team.aliens.dms.global.security.exception.InvalidTokenException
import java.util.UUID

@Component
class StudentDetailsService(
    private val queryUserPort: QueryUserPort
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        queryUserPort.queryUserById(UUID.fromString(username)).apply {
            if (this == null || authority != Authority.STUDENT) {
                throw InvalidTokenException
            }

            return StudentDetails(
                userId = id,
                schoolId = schoolId,
                authority = authority
            )
        }
    }
}

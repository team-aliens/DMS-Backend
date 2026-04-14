package team.aliens.dms.global.security.principle

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.user.spi.QueryUserPort
import team.aliens.dms.global.security.exception.InvalidTokenException
import java.util.UUID

class TeacherDetailsService(
    private val queryUserPort: QueryUserPort,
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        queryUserPort.queryUserById(UUID.fromString(username)).apply {
            if (this == null || authority != Authority.TEACHER) {
                throw InvalidTokenException
            }

            return TeacherDetails(
                userId = id,
                schoolId = schoolId,
                authority = authority
            )
        }
    }
}

package team.aliens.dms.global.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.global.security.principle.CustomDetails
import java.util.UUID

@Component
class SecurityAdapter(
    private val passwordEncoder: PasswordEncoder
) : SecurityPort {

    override fun isPasswordMatch(rawPassword: String, encodedPassword: String) = passwordEncoder.matches(
        rawPassword, encodedPassword
    )

    override fun isAuthenticated() =
        SecurityContextHolder.getContext().authentication.principal is CustomDetails

    override fun getCurrentUserId(): UUID {
        return (SecurityContextHolder.getContext().authentication.principal as CustomDetails).userId
    }

    override fun getCurrentUserSchoolId(): UUID {
        return (SecurityContextHolder.getContext().authentication.principal as CustomDetails).schoolId
    }

    override fun encodePassword(password: String): String = passwordEncoder.encode(password)

    override fun isStudent(): Boolean {
        val authority = (SecurityContextHolder.getContext().authentication.principal as CustomDetails).authority
        return authority == Authority.STUDENT
    }
}

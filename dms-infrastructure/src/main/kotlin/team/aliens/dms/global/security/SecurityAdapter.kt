package team.aliens.dms.global.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.global.security.principle.CustomDetails
import java.util.UUID

@Component
class SecurityAdapter(
    private val passwordEncoder: PasswordEncoder
) : SecurityPort {

    override fun getCurrentUserId(): UUID {
        return (SecurityContextHolder.getContext().authentication.principal as CustomDetails).userId
    }

    override fun getCurrentUserSchoolId(): UUID {
        return (SecurityContextHolder.getContext().authentication.principal as CustomDetails).schoolId
    }

    override fun isPasswordMatch(rawPassword: String, encodedPassword: String) = passwordEncoder.matches(
        rawPassword, encodedPassword
    )

    override fun encodePassword(password: String): String = passwordEncoder.encode(password)
}

package team.aliens.dms.global.security.principle

import java.util.UUID
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.global.security.exception.InvalidTokenException

@Component
class StudentDetailsService(
    private val queryStudentPort: QueryStudentPort
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val studentUserId = UUID.fromString(username)
        if (!queryStudentPort.existsStudentByUserId(studentUserId)) {
            throw InvalidTokenException
        }
        return StudentDetails(studentUserId)
    }
}

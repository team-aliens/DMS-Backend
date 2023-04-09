package team.aliens.dms.global.security.principle

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.global.security.exception.InvalidTokenException
import java.util.UUID

@Component
class StudentDetailsService(
    private val queryStudentPort: QueryStudentPort
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val studentId = UUID.fromString(username)
        if (!queryStudentPort.existsStudentById(studentId)) {
            throw InvalidTokenException
        }
        return StudentDetails(studentId)
    }
}

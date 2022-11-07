package team.aliens.dms.global.security.principle

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import team.aliens.dms.global.security.exception.InvalidTokenException
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import java.util.UUID

@Component
class StudentDetailsService(
    private val studentRepository: StudentJpaRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val student = studentRepository.findByIdOrNull(UUID.fromString(username)) ?: throw InvalidTokenException

        return StudentDetails(student.userId)
    }
}
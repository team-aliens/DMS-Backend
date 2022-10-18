package team.aliens.dms.global.security.principle

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import team.aliens.dms.global.security.exception.InvalidTokenException
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import java.util.*

@Component
class StudentDetailsService(
    private val studentRepository: StudentJpaRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val studentId = UUID.fromString(username)
        return studentRepository.findByIdOrNull(studentId)?.let {
            StudentDetails(studentId)
        } ?: throw InvalidTokenException
    }
}
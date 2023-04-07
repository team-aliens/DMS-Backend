package team.aliens.dms.global.security.principle

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.global.security.exception.InvalidTokenException
import java.util.UUID

@Component
class StudentDetailsService(
    private val queryStudentPort: StudyRoomQueryStudentPort
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val student = queryStudentPort.queryStudentById(UUID.fromString(username)) ?: throw InvalidTokenException

        return StudentDetails(student.id)
    }
}

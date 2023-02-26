package team.aliens.dms.global.security.principle

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import team.aliens.dms.global.security.exception.InvalidTokenException
import team.aliens.dms.persistence.manager.repository.ManagerJpaRepository
import java.util.UUID

@Component
class ManagerDetailsService(
    private val managerRepository: ManagerJpaRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val manager = managerRepository.findByIdOrNull(UUID.fromString(username)) ?: throw InvalidTokenException

        return ManagerDetails(manager.id)
    }
}

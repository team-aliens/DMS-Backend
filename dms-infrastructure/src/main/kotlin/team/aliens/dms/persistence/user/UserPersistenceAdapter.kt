package team.aliens.dms.persistence.user

import org.springframework.stereotype.Component
import team.aliens.dms.domain.user.spi.UserPort
import team.aliens.dms.persistence.user.mapper.UserMapper
import team.aliens.dms.persistence.user.repository.UserJpaRepository

@Component
class UserPersistenceAdapter(
    private val userJpaRepository: UserJpaRepository
) : UserPort {

    override fun existsByEmail(email: String): Boolean {
        val user = userJpaRepository.findByEmail(email)

        user?.let {
            return true
        }
        return false
    }
}
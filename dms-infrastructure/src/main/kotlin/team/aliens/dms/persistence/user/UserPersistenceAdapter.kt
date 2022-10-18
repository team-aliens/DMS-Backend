package team.aliens.dms.persistence.user

import org.springframework.stereotype.Component
import team.aliens.dms.domain.user.spi.UserPort
import team.aliens.dms.persistence.user.repository.UserJpaRepository

@Component
class UserPersistenceAdapter(
    private val userJpaRepository: UserJpaRepository
) : UserPort {

    override fun existsByEmail(email: String) = userJpaRepository.existsByEmail(email)
}
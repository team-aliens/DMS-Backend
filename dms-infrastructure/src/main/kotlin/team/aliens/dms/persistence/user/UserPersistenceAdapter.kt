package team.aliens.dms.persistence.user

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.spi.UserPort
import team.aliens.dms.persistence.user.mapper.UserMapper
import team.aliens.dms.persistence.user.repository.UserJpaRepository
import java.util.*

@Component
class UserPersistenceAdapter(
    val userJpaRepository: UserJpaRepository,
    val userMapper: UserMapper
) : UserPort {

    override fun queryUserById(id: UUID) = userMapper.toDomain(
        userJpaRepository.findByIdOrNull(id)
    )
}
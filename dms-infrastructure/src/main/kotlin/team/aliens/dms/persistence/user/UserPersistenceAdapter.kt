package team.aliens.dms.persistence.user

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.spi.UserPort
import team.aliens.dms.persistence.user.mapper.UserMapper
import team.aliens.dms.persistence.user.repository.UserJpaRepository
import java.util.*

@Component
class UserPersistenceAdapter(
    private val userMapper: UserMapper,
    private val userRepository: UserJpaRepository,
) : UserPort {

    override fun existsByEmail(email: String) = userRepository.existsByEmail(email)

    override fun existsByAccountId(accountId: String): Boolean = userRepository.existsByAccountId(accountId)

    override fun queryByUserId(id: UUID) = userMapper.toDomain(
        userRepository.findByIdOrNull(id)
    )

    override fun queryByAccountId(accountId: String) = userMapper.toDomain(
        userRepository.findByAccountId(accountId)
    )

    override fun saveUser(user: User) = userMapper.toDomain(
        userRepository.save(
            userMapper.toEntity(user)
        )
    )!!

    override fun queryUserById(id: UUID) = userMapper.toDomain(
        userRepository.findByIdOrNull(id)
    )

    override fun queryUserBySchoolId(schoolId: UUID) = userMapper.toDomain(
        userRepository.findBySchoolId(schoolId)
    )

    override fun queryUserByEmail(email: String) = userMapper.toDomain(
        userRepository.findByEmail(email)
    )

    override fun queryUserByAccountId(accountId: String) = userMapper.toDomain(
        userRepository.findByAccountId(accountId)
    )

    override fun searchStudent(name: String, sort: Sort): List<User> {
        //TODO #71머지이후 querydsl 이용
    }
}
 

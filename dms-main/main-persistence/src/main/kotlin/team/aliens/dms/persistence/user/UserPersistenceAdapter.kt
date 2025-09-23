package team.aliens.dms.persistence.user

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.spi.UserPort
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.user.entity.QUserJpaEntity.userJpaEntity
import team.aliens.dms.persistence.user.mapper.UserMapper
import team.aliens.dms.persistence.user.repository.UserJpaRepository
import java.util.UUID

@Component
class UserPersistenceAdapter(
    private val userMapper: UserMapper,
    private val userRepository: UserJpaRepository,
    private val queryFactory: JPAQueryFactory
) : UserPort {

    override fun existsUserByEmail(email: String): Boolean {
        return userRepository.findExistsByEmail(email) != null
    }

    override fun existsUserByAccountId(accountId: String): Boolean {
        return userRepository.findExistsByAccountId(accountId) != null
    }

    override fun saveUser(user: User) = userMapper.toDomain(
        userRepository.save(
            userMapper.toEntity(user)
        )
    )!!

    override fun queryUserById(userId: UUID) = userMapper.toDomain(
        userRepository.findByIdOrNull(userId)
    )

    override fun queryUserBySchoolIdAndAuthority(schoolId: UUID, authority: Authority) = userMapper.toDomain(
        userRepository.findBySchoolIdAndAuthority(schoolId, authority)
    )

    override fun queryUsersBySchoolId(schoolId: UUID) =
        userRepository.findBySchoolId(schoolId)
            .map { userMapper.toDomain(it)!! }

    override fun queryUsersByStudentIds(studentIds: List<UUID>): List<User> =
        queryFactory
            .selectFrom(userJpaEntity)
            .innerJoin(studentJpaEntity)
            .on(userJpaEntity.eq(studentJpaEntity.user))
            .where(studentJpaEntity.id.`in`(studentIds))
            .fetch()
            .map { userMapper.toDomain(it)!! }

    override fun queryUserByEmail(email: String) = userMapper.toDomain(
        userRepository.findByEmail(email)
    )

    override fun queryUserByAccountId(accountId: String) = userMapper.toDomain(
        userRepository.findByAccountId(accountId)
    )
}

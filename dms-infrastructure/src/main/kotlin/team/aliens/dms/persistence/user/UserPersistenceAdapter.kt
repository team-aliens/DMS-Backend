package team.aliens.dms.persistence.user

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.spi.UserPort
import team.aliens.dms.persistence.user.mapper.UserMapper
import team.aliens.dms.persistence.user.repository.UserJpaRepository
import java.util.UUID

import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.user.entity.QUserJpaEntity.userJpaEntity

@Component
class UserPersistenceAdapter(
    private val userMapper: UserMapper,
    private val userRepository: UserJpaRepository,
    private val queryFactory: JPAQueryFactory
) : UserPort {

    override fun existsUserByEmail(email: String) = userRepository.existsByEmail(email)

    override fun existsUserByAccountId(accountId: String): Boolean = userRepository.existsByAccountId(accountId)

    override fun saveUser(user: User) = userMapper.toDomain(
        userRepository.save(
            userMapper.toEntity(user)
        )
    )!!

    override fun queryUserById(userId: UUID) = userMapper.toDomain(
        userRepository.findByIdOrNull(userId)
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

    override fun queryUserByRoomNumberAndSchoolId(roomNumber: Int, schoolId: UUID): List<User> {
        return queryFactory
            .selectFrom(userJpaEntity)
            .join(studentJpaEntity).on(userJpaEntity.id.eq(studentJpaEntity.userId))
            .where(
                studentJpaEntity.room.id.roomNumber.eq(roomNumber),
                studentJpaEntity.room.id.schoolId.eq(schoolId)
            ).fetch()
            .map {
                userMapper.toDomain(it)!!
            }
    }
}
 

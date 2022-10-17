package team.aliens.dms.persistence.user.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.user.entity.UserJpaEntity

@Component
class UserMapper(
    private val schoolJpaRepository: SchoolJpaRepository
) : GenericMapper<User, UserJpaEntity> {

    override fun toDomain(entity: UserJpaEntity?): User? {
        val school = entity?.school?.let {
            schoolJpaRepository.findByIdOrNull(it.id)
        } ?: throw SchoolNotFoundException

        return User(
            id = entity.id,
            schoolId = school.id,
            accountId = entity.accountId,
            password = entity.password,
            email = entity.email,
            name = entity.name,
            profileImageUrl = entity.profileImageUrl,
            createdAt = entity.createdAt,
            deletedAt = entity.deletedAt
        )
    }

    override fun toEntity(domain: User): UserJpaEntity {
        val school = schoolJpaRepository.findByIdOrNull(domain.schoolId) ?: throw SchoolNotFoundException

        return UserJpaEntity(
            id = domain.id ?: throw UserNotFoundException,
            school = school,
            accountId = domain.accountId,
            password = domain.password,
            email = domain.email,
            name = domain.name,
            profileImageUrl = domain.profileImageUrl,
            createdAt = domain.createdAt ?: throw UserNotFoundException,
            deletedAt = domain.deletedAt
        )
    }
}

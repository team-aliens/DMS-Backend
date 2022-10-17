package team.aliens.dms.persistence.user.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.user.entity.UserJpaEntity

@Component
class UserMapper(
    private val schoolJpaRepository: SchoolJpaRepository
) : GenericMapper<User, UserJpaEntity> {

    override fun toDomain(entity: UserJpaEntity?): User? {
        return User(
            id = entity!!.id,
            schoolId = entity.school!!.id,
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
        val school = schoolJpaRepository.findByIdOrNull(domain.schoolId)

        return UserJpaEntity(
            id = domain.id,
            school = school,
            accountId = domain.accountId,
            password = domain.password,
            email = domain.email,
            name = domain.name,
            profileImageUrl = domain.profileImageUrl,
            createdAt = domain.createdAt!!,
            deletedAt = domain.deletedAt
        )
    }
}

package team.aliens.dms.persistence.user.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolRepository
import team.aliens.dms.persistence.user.entity.UserEntity

@Component
class UserMapper(
    private val schoolRepository: SchoolRepository
) : GenericMapper<User, UserEntity> {

    override fun toDomain(e: UserEntity): User {
        val school = e.schoolEntity?.let {
            schoolRepository.findByIdOrNull(it.id)
        } ?: throw RuntimeException()

        return User(
            id = e.id,
            schoolId = school.id,
            accountId = e.accountId,
            password = e.password,
            email = e.email,
            name = e.name,
            profileImageUrl = e.profileImageUrl,
            createdAt = e.createdAt,
            deletedAt = e.deletedAt
        )
    }

    override fun toEntity(d: User): UserEntity {
        val school = schoolRepository.findByIdOrNull(d.schoolId) ?: throw RuntimeException()

        return UserEntity(
            id = d.id,
            schoolEntity = school,
            accountId = d.accountId,
            password = d.password,
            email = d.email,
            name = d.name,
            profileImageUrl = d.profileImageUrl,
            createdAt = d.createdAt,
            deletedAt = d.deletedAt
        )
    }
}

package team.aliens.dms.persistence.notification.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.notification.entity.DeviceTokenJpaEntity
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.user.repository.UserJpaRepository

@Component
class DeviceTokenMapper(
    private val userRepository: UserJpaRepository,
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<DeviceToken, DeviceTokenJpaEntity> {

    override fun toDomain(entity: DeviceTokenJpaEntity?): DeviceToken? {
        return entity?.let {
            DeviceToken(
                id = it.id!!,
                userId = it.user!!.id!!,
                schoolId = it.school!!.id!!,
                token = it.token
            )
        }
    }

    override fun toEntity(domain: DeviceToken): DeviceTokenJpaEntity {

        val user = userRepository.findByIdOrNull(domain.userId) ?: throw UserNotFoundException
        val school = schoolRepository.findByIdOrNull(domain.schoolId) ?: throw SchoolNotFoundException

        return DeviceTokenJpaEntity(
            id = domain.id,
            user = user,
            school = school,
            token = domain.token
        )
    }
}

package team.aliens.dms.persistence.notification.mapper

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.notification.entity.DeviceTokenJpaEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import team.aliens.dms.persistence.user.entity.UserJpaEntity

@Component
class DeviceTokenMapper(
    private val entityManager: EntityManager
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
        return DeviceTokenJpaEntity(
            id = domain.id,
            user = entityManager.getReference(UserJpaEntity::class.java, domain.userId),
            school = entityManager.getReference(SchoolJpaEntity::class.java, domain.schoolId),
            token = domain.token
        )
    }
}

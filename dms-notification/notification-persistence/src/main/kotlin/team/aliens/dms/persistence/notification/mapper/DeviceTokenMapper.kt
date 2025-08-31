package team.aliens.dms.persistence.notification.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.notification.entity.DeviceTokenJpaEntity

@Component
class DeviceTokenMapper() : GenericMapper<DeviceToken, DeviceTokenJpaEntity> {

    override fun toDomain(entity: DeviceTokenJpaEntity?): DeviceToken? {
        return entity?.let {
            DeviceToken(
                id = it.id!!,
                userId = it.userId!!,
                schoolId = it.schoolId!!,
                token = it.token
            )
        }
    }

    override fun toEntity(domain: DeviceToken): DeviceTokenJpaEntity {

        return DeviceTokenJpaEntity(
            id = domain.id,
            userId = domain.userId,
            schoolId = domain.schoolId,
            token = domain.token
        )
    }
}

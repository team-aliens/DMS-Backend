package team.aliens.dms.persistence.notification

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.spi.NotificationTokenPort
import team.aliens.dms.persistence.notification.mapper.DeviceTokenMapper
import team.aliens.dms.persistence.notification.repository.DeviceTokenJpaRepository
import java.util.UUID

@Component
class DeviceTokenPersistenceAdapter(
    private val notificationMapper: DeviceTokenMapper,
    private val notificationTokenRepository: DeviceTokenJpaRepository
) : NotificationTokenPort {

    override fun saveNotificationToken(deviceToken: DeviceToken) = notificationMapper.toDomain(
        notificationTokenRepository.save(
            notificationMapper.toEntity(deviceToken)
        )
    )!!

    override fun deleteNotificationTokenById(userId: UUID) {
        notificationTokenRepository.deleteById(userId)
    }

    override fun queryNotificationTokenById(userId: UUID) = notificationMapper.toDomain(
        notificationTokenRepository.findByIdOrNull(userId)
    )
}

package team.aliens.dms.persistence.notification

import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.persistence.notification.mapper.DeviceTokenMapper
import team.aliens.dms.persistence.notification.repository.DeviceTokenJpaRepository

@Component
class DeviceTokenPersistenceAdapter(
    private val notificationMapper: DeviceTokenMapper,
    private val deviceTokenRepository: DeviceTokenJpaRepository
) : DeviceTokenPort {

    override fun saveDeviceToken(deviceToken: DeviceToken) = notificationMapper.toDomain(
        deviceTokenRepository.save(
            notificationMapper.toEntity(deviceToken)
        )
    )!!

    override fun deleteDeviceTokenById(userId: UUID) {
        deviceTokenRepository.deleteById(userId)
    }

    override fun queryDeviceTokenById(userId: UUID) = notificationMapper.toDomain(
        deviceTokenRepository.findByIdOrNull(userId)
    )

    override fun queryDeviceToenByDeviceToken(deviceToken: String) = notificationMapper.toDomain(
        deviceTokenRepository.queryByDeviceToken(deviceToken)
    )
}

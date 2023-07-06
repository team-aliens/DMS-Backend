package team.aliens.dms.persistence.notification

import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.OperatingSystem
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.persistence.notification.mapper.DeviceTokenMapper
import team.aliens.dms.persistence.notification.repository.DeviceTokenJpaRepository
import java.util.UUID

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

    override fun queryDeviceTokenByUserId(userId: UUID) = notificationMapper.toDomain(
        deviceTokenRepository.findByUserId(userId)
    )

    override fun queryDeviceTokenByToken(token: String) = notificationMapper.toDomain(
        deviceTokenRepository.findByToken(token)
    )

    override fun queryDeviceTokenByOSAndDeviceId(operatingSystem: OperatingSystem, deviceId: String) = notificationMapper.toDomain(
        deviceTokenRepository.findByOperatingSystemAndDeviceId(operatingSystem, deviceId)
    )

    override fun deleteDeviceTokenByUserId(userId: UUID) {
        deviceTokenRepository.deleteByUserId(userId)
    }

    override fun deleteDeviceTokenById(deviceTokenId: UUID) {
        deviceTokenRepository.deleteById(deviceTokenId)
    }
}

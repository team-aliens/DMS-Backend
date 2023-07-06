package team.aliens.dms.persistence.notification.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.domain.notification.model.OperatingSystem
import team.aliens.dms.persistence.notification.entity.DeviceTokenJpaEntity
import java.util.UUID

@Repository
interface DeviceTokenJpaRepository : CrudRepository<DeviceTokenJpaEntity, UUID> {
    fun findByToken(token: String): DeviceTokenJpaEntity?
    fun findByUserId(userId: UUID): DeviceTokenJpaEntity?
    fun findByOperatingSystemAndDeviceId(operatingSystem: OperatingSystem, deviceId: String): DeviceTokenJpaEntity?
    fun deleteByUserId(userId: UUID)
}

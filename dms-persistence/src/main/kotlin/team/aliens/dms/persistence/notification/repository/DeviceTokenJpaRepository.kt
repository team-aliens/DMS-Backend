package team.aliens.dms.persistence.notification.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.notification.entity.DeviceTokenJpaEntity

@Repository
interface DeviceTokenJpaRepository : CrudRepository<DeviceTokenJpaEntity, UUID> {
    fun queryByDeviceToken(deviceToken: String): DeviceTokenJpaEntity?
}

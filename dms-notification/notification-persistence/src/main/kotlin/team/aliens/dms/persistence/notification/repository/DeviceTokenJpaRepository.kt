package team.aliens.dms.persistence.notification.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.notification.entity.DeviceTokenJpaEntity
import java.util.UUID

@Repository
interface DeviceTokenJpaRepository : CrudRepository<DeviceTokenJpaEntity, UUID> {
    fun findByToken(token: String): DeviceTokenJpaEntity?

    fun findByUserId(userId: UUID): DeviceTokenJpaEntity?

    fun deleteByUserId(userId: UUID)

    fun existsByUserId(userId: UUID): Boolean
}

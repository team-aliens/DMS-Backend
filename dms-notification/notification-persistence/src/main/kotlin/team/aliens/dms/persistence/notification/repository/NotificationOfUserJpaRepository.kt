package team.aliens.dms.persistence.notification.repository

import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.notification.entity.NotificationOfUserJpaEntity
import java.util.UUID

interface NotificationOfUserJpaRepository : CrudRepository<NotificationOfUserJpaEntity, UUID> {
    fun findByUserId(userId: UUID): List<NotificationOfUserJpaEntity>
    fun deleteByUserId(userId: UUID)
}

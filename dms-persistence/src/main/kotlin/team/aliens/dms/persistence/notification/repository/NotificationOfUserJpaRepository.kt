package team.aliens.dms.persistence.notification.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.notification.entity.NotificationOfUserJpaEntity

interface NotificationOfUserJpaRepository : CrudRepository<NotificationOfUserJpaEntity, UUID> {
    fun findByUserId(userId: UUID): List<NotificationOfUserJpaEntity>
}

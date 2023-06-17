package team.aliens.dms.persistence.notification.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.notification.entity.UserNotificationJpaEntity

interface UserNotificationJpaRepository : CrudRepository<UserNotificationJpaEntity, UUID> {
    fun findByUserId(userId: UUID): List<UserNotificationJpaEntity>
}

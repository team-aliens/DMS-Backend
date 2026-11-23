package team.aliens.dms.persistence.notification.repository

import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.notification.entity.NotificationOutboxJpaEntity
import java.util.UUID

interface NotificationOutboxJpaRepository : CrudRepository<NotificationOutboxJpaEntity, UUID>

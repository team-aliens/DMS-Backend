package team.aliens.dms.persistence.notification.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.notification.entity.TopicSubscriptionJpaEntity
import team.aliens.dms.persistence.notification.entity.TopicSubscriptionJpaEntityId
import java.util.UUID

@Repository
interface TopicSubscriptionJpaRepository : CrudRepository<TopicSubscriptionJpaEntity, TopicSubscriptionJpaEntityId> {
    fun findByDeviceTokenId(deviceTokenId: UUID): List<TopicSubscriptionJpaEntity>
}

package team.aliens.dms.persistence.notification.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.notification.entity.TopicSubscribeJpaEntity
import team.aliens.dms.persistence.notification.entity.TopicSubscribeJpaEntityId
import java.util.UUID

@Repository
interface TopicSubscribeJpaRepository : CrudRepository<TopicSubscribeJpaEntity, TopicSubscribeJpaEntityId> {
    fun findByDeviceTokenId(deviceTokenId: UUID): List<TopicSubscribeJpaEntity>
}

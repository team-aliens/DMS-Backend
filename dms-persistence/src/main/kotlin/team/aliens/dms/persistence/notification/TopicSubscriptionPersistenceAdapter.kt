package team.aliens.dms.persistence.notification

import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.TopicSubscription
import team.aliens.dms.domain.notification.spi.TopicSubscriptionPort
import team.aliens.dms.persistence.notification.mapper.TopicSubscriptionMapper
import team.aliens.dms.persistence.notification.repository.TopicSubscriptionJpaRepository
import java.util.UUID

@Component
class TopicSubscriptionPersistenceAdapter(
    private val topicSubscriptionMapper: TopicSubscriptionMapper,
    private val topicSubscriptionRepository: TopicSubscriptionJpaRepository
) : TopicSubscriptionPort {

    override fun saveTopicSubscription(topicSubscription: TopicSubscription) = topicSubscriptionMapper.toDomain(
        topicSubscriptionRepository.save(
            topicSubscriptionMapper.toEntity(topicSubscription)
        )
    )!!

    override fun saveAllTopicSubscriptions(topicSubscriptions: List<TopicSubscription>) {
        topicSubscriptionRepository.saveAll(
            topicSubscriptions.map {
                topicSubscriptionMapper.toEntity(it)
            }
        )
    }

    override fun queryTopicSubscriptionsByDeviceTokenId(deviceTokenId: UUID) =
        topicSubscriptionRepository.findByDeviceTokenId(deviceTokenId)
            .map { topicSubscriptionMapper.toDomain(it)!! }
}

package team.aliens.dms.persistence.notification

import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.model.TopicSubscribe
import team.aliens.dms.domain.notification.spi.TopicSubscribePort
import team.aliens.dms.persistence.notification.entity.TopicSubscribeJpaEntityId
import team.aliens.dms.persistence.notification.mapper.TopicSubscribeMapper
import team.aliens.dms.persistence.notification.repository.TopicSubscribeJpaRepository
import java.util.UUID

@Component
class TopicSubscribePersistenceAdapter(
    private val topicSubscribeMapper: TopicSubscribeMapper,
    private val topicSubscribeRepository: TopicSubscribeJpaRepository
) : TopicSubscribePort {

    override fun saveTopicSubscribe(topicSubscribe: TopicSubscribe) = topicSubscribeMapper.toDomain(
        topicSubscribeRepository.save(
            topicSubscribeMapper.toEntity(topicSubscribe)
        )
    )!!

    override fun saveAllTopicSubscribes(topicSubscribes: List<TopicSubscribe>) {
        topicSubscribeRepository.saveAll(
            topicSubscribes.map {
                topicSubscribeMapper.toEntity(it)
            }
        )
    }

    override fun deleteByUserIdAndTopics(userId: UUID, topics: List<Topic>) {
        topicSubscribeRepository.deleteAllById(
            topics.map {
                TopicSubscribeJpaEntityId(
                    deviceTokenId = userId,
                    topic = it
                )
            }
        )
    }

    override fun queryTopicSubscribesByDeviceTokenId(deviceTokenId: UUID) =
        topicSubscribeRepository.findByDeviceTokenId(deviceTokenId)
            .map { topicSubscribeMapper.toDomain(it)!! }
}

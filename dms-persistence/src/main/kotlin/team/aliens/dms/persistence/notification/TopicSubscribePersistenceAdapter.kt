package team.aliens.dms.persistence.notification

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.model.TopicSubscribe
import team.aliens.dms.domain.notification.spi.TopicSubscribePort
import team.aliens.dms.persistence.notification.entity.QTopicSubscribeJpaEntity.topicSubscribeJpaEntity
import team.aliens.dms.persistence.notification.mapper.TopicSubscribeMapper
import team.aliens.dms.persistence.notification.repository.TopicSubscribeJpaRepository
import java.util.UUID

@Component
class TopicSubscribePersistenceAdapter(
    private val queryFactory: JPAQueryFactory,
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

    override fun deleteByDeviceTokenIdAndTopics(deviceTokenId: UUID, topics: List<Topic>) {
        queryFactory
            .delete(topicSubscribeJpaEntity)
            .where(
                topicSubscribeJpaEntity.deviceToken.id.eq(deviceTokenId),
                topicSubscribeJpaEntity.id.topic.`in`(topics)
            )
            .execute()
    }

    override fun queryTopicSubscribesByDeviceTokenId(deviceTokenId: UUID) =
        topicSubscribeRepository.findByDeviceTokenId(deviceTokenId)
            .map { topicSubscribeMapper.toDomain(it)!! }
}

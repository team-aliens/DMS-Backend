package team.aliens.dms.persistence.notification.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.model.TopicSubscribe
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.notification.entity.TopicSubscribeJpaEntity
import team.aliens.dms.persistence.notification.entity.TopicSubscribeJpaEntityId
import team.aliens.dms.persistence.notification.repository.DeviceTokenJpaRepository

@Component
class TopicSubscribeMapper(
    private val deviceTokenRepository: DeviceTokenJpaRepository
) : GenericMapper<TopicSubscribe, TopicSubscribeJpaEntity> {

    override fun toDomain(entity: TopicSubscribeJpaEntity?): TopicSubscribe? {
        return entity?.let {
            TopicSubscribe(
                deviceTokenId = it.deviceToken.id!!,
                topic = it.id.topic,
                isSubscribed = it.isSubscribed
            )
        }
    }

    override fun toEntity(domain: TopicSubscribe): TopicSubscribeJpaEntity {

        val deviceToken = deviceTokenRepository.findByIdOrNull(domain.deviceTokenId) ?: throw DeviceTokenNotFoundException

        return TopicSubscribeJpaEntity(
            id = TopicSubscribeJpaEntityId(
                deviceTokenId = domain.deviceTokenId,
                topic = domain.topic
            ),
            deviceToken = deviceToken,
            isSubscribed = domain.isSubscribed
        )
    }
}

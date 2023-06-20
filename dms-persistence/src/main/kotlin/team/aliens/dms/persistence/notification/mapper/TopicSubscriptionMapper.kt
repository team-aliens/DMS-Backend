package team.aliens.dms.persistence.notification.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.model.TopicSubscription
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.notification.entity.TopicSubscriptionJpaEntityId
import team.aliens.dms.persistence.notification.entity.TopicSubscriptionJpaEntity
import team.aliens.dms.persistence.notification.repository.DeviceTokenJpaRepository

@Component
class TopicSubscriptionMapper(
    private val deviceTokenRepository: DeviceTokenJpaRepository
) : GenericMapper<TopicSubscription, TopicSubscriptionJpaEntity> {

    override fun toDomain(entity: TopicSubscriptionJpaEntity?): TopicSubscription? {
        return entity?.let {
            TopicSubscription(
                deviceTokenId = it.deviceToken.id!!,
                topic = it.id.topic,
                isSubscribed = it.isSubscribed
            )
        }
    }

    override fun toEntity(domain: TopicSubscription): TopicSubscriptionJpaEntity {

        val deviceToken = deviceTokenRepository.findByIdOrNull(domain.deviceTokenId) ?: throw DeviceTokenNotFoundException

        return TopicSubscriptionJpaEntity(
            id = TopicSubscriptionJpaEntityId(
                deviceTokenId = domain.deviceTokenId,
                topic = domain.topic
            ),
            deviceToken = deviceToken,
            isSubscribed = domain.isSubscribed
        )
    }
}

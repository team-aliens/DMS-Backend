package team.aliens.dms.domain.notification.spi

import java.util.UUID
import team.aliens.dms.domain.notification.model.TopicSubscription

interface QueryTopicSubscriptionPort {

    fun queryTopicSubscriptionsByDeviceTokenId(deviceTokenId: UUID): List<TopicSubscription>
}
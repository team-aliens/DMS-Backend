package team.aliens.dms.domain.notification.spi

import team.aliens.dms.domain.notification.model.TopicSubscription
import java.util.UUID

interface QueryTopicSubscriptionPort {

    fun queryTopicSubscriptionsByDeviceTokenId(deviceTokenId: UUID): List<TopicSubscription>
}

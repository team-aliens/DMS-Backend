package team.aliens.dms.domain.notification.spi

import java.util.UUID
import team.aliens.dms.domain.notification.model.TopicSubscription

interface TopicSubscriptionPort {

    fun saveTopicSubscription(topicSubscription: TopicSubscription): TopicSubscription

    fun queryTopicSubscriptionsByDeviceTokenId(deviceTokenId: UUID): List<TopicSubscription>

    fun saveAllTopicSubscriptions(topicSubscriptions: List<TopicSubscription>)
}

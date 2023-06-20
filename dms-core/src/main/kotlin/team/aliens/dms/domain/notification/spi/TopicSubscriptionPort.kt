package team.aliens.dms.domain.notification.spi

import team.aliens.dms.domain.notification.model.TopicSubscription
import java.util.UUID

interface TopicSubscriptionPort {

    fun saveTopicSubscription(topicSubscription: TopicSubscription): TopicSubscription

    fun queryTopicSubscriptionsByDeviceTokenId(deviceTokenId: UUID): List<TopicSubscription>

    fun saveAllTopicSubscriptions(topicSubscriptions: List<TopicSubscription>)
}

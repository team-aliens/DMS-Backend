package team.aliens.dms.stub

import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.TopicSubscription
import java.util.UUID

internal fun createTopicSubscriptionStub(
    deviceTokenId: UUID = UUID.randomUUID(),
    topic: Topic = Topic.NOTICE,
    isSubscribed: Boolean = true
) = TopicSubscription(
    deviceTokenId = deviceTokenId,
    topic = topic,
    isSubscribed = isSubscribed
)

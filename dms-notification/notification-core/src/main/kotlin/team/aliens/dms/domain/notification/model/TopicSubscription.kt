package team.aliens.dms.domain.notification.model

import java.util.UUID

data class TopicSubscription(

    val deviceTokenId: UUID,

    val topic: Topic,

    val isSubscribed: Boolean

) {
    companion object {

        fun subscribe(
            deviceTokenId: UUID,
            topic: Topic,
        ) = TopicSubscription(
            deviceTokenId = deviceTokenId,
            topic = topic,
            isSubscribed = true
        )

        fun unsubscribe(
            deviceTokenId: UUID,
            topic: Topic,
        ) = TopicSubscription(
            deviceTokenId = deviceTokenId,
            topic = topic,
            isSubscribed = false
        )
    }
}

package team.aliens.dms.domain.notification.model

import java.util.UUID

data class TopicSubscribe(

    val deviceTokenId: UUID,

    val topic: Topic,

    val isSubscribed: Boolean

)

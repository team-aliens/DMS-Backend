package team.aliens.dms.domain.notification.dto

import team.aliens.dms.domain.notification.model.Topic

data class TopicSubscribesResponse(
    val topicSubscribes: List<TopicSubscribeResponse>
)

data class TopicSubscribeResponse(
    val topic: Topic,
    val isSubscribed: Boolean
)

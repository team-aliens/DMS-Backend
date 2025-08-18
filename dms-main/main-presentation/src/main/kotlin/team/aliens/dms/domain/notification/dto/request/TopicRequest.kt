package team.aliens.dms.domain.notification.dto.request

import team.aliens.dms.domain.notification.model.Topic

data class TopicRequest(
    val deviceToken: String,
    val topic: Topic
)

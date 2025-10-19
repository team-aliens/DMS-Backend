package team.aliens.dms.domain.notification.dto.request

import team.aliens.dms.contract.model.notification.Topic

data class TopicRequest(
    val deviceToken: String,
    val topic: Topic
)

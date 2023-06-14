package team.aliens.dms.domain.notification.dto.request

import team.aliens.dms.domain.notification.model.Topic

data class UpdateTopicSubscribesWebRequest(
    val topicsToSubscribe: List<TopicsToSubscribe>
) {
    data class TopicsToSubscribe(
        val topic: Topic,
        val isSubscribe: Boolean
    ) {
        fun toPair() = Pair(topic, isSubscribe)
    }
}
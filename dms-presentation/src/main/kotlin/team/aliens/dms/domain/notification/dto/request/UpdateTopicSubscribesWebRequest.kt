package team.aliens.dms.domain.notification.dto.request

import team.aliens.dms.domain.notification.model.Topic

data class UpdateTopicSubscribesWebRequest(
    val topicSubscribes: List<TopicSubscribes>
) {
    data class TopicSubscribes(
        val topic: Topic,
        val isSubscribe: Boolean
    ) {
        fun toPair() = Pair(topic, isSubscribe)
    }
}
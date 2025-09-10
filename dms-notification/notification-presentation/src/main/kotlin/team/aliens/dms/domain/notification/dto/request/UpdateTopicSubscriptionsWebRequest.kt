package team.aliens.dms.domain.notification.dto.request

import team.aliens.dms.contract.model.Topic

data class UpdateTopicSubscriptionsWebRequest(
    val deviceToken: String,
    val topicsToSubscribe: List<TopicsToSubscribe>
) {
    data class TopicsToSubscribe(
        val topic: Topic,
        val isSubscribe: Boolean
    ) {
        fun toPair() = Pair(topic, isSubscribe)
    }
}

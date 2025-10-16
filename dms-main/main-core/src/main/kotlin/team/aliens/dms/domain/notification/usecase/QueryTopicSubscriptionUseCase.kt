package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notification.dto.TopicSubscriptionGroupsResponse
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.service.NotificationService

@UseCase
class QueryTopicSubscriptionUseCase(
    private val notificationService: NotificationService
) {

    fun execute(token: String): TopicSubscriptionGroupsResponse {
        val topicSubscriptions = notificationService.getTopicSubscriptionsByToken(token)

        notificationService.updateSubscribes(
            token = token,
            topicsToSubscribe = Topic.values().filter { topic ->
                !topicSubscriptions.any { it.topic.name == topic.name }
            }.map { topic ->
                Pair(
                    topic,
                    true
                )
            }
        )

        return TopicSubscriptionGroupsResponse.of(
            topicSubscriptions = topicSubscriptions
        )
    }
}

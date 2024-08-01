package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notification.dto.TopicSubscriptionGroupsResponse
import team.aliens.dms.domain.notification.model.TopicGroup
import team.aliens.dms.domain.notification.service.NotificationService

@UseCase
class QueryTopicSubscriptionUseCase(
    private val notificationService: NotificationService
) {

    fun execute(token: String): TopicSubscriptionGroupsResponse {
        val topicSubscriptions = notificationService.getTopicSubscriptionsByToken(token)

        notificationService.updateSubscribes(
            token = token,
            topicsToSubscribe = topicSubscriptions.filter { topicSubscription ->
                TopicGroup.values().any { topicSubscription.topic.name == it.name }
            }.map { topicSubscription ->
                Pair(
                    topicSubscription.topic,
                    true
                )
            }
        )

        return TopicSubscriptionGroupsResponse.of(
            topicSubscriptions = topicSubscriptions
        )
    }
}

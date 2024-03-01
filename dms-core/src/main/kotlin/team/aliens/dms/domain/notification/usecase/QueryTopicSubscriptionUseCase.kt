package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notification.dto.TopicSubscriptionGroupsResponse
import team.aliens.dms.domain.notification.service.NotificationService

@ReadOnlyUseCase
class QueryTopicSubscriptionUseCase(
    private val notificationService: NotificationService
) {

    fun execute(token: String): TopicSubscriptionGroupsResponse {
        return TopicSubscriptionGroupsResponse.of(
            notificationService.getTopicSubscriptionsByToken(token)
        )
    }
}

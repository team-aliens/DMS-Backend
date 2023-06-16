package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notification.dto.TopicSubscribesResponse
import team.aliens.dms.domain.notification.service.NotificationService

@ReadOnlyUseCase
class QueryTopicSubscribesUseCase(
    private val notificationService: NotificationService
) {

    fun execute(deviceToken: String): TopicSubscribesResponse {
        return TopicSubscribesResponse(
            notificationService.getTopicSubscribesByDeviceToken(deviceToken)
        )
    }
}

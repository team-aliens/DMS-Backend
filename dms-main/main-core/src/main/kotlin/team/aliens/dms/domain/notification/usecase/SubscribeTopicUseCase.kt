package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.service.NotificationService

@UseCase
class SubscribeTopicUseCase(
    private val notificationService: NotificationService
) {

    fun execute(deviceToken: String, topic: Topic) {
        notificationService.subscribeTopic(
            token = deviceToken,
            topic = topic
        )
    }
}

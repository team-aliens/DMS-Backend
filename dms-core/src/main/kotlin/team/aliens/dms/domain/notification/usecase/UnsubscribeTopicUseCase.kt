package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.service.NotificationService

@UseCase
class UnsubscribeTopicUseCase(
    private val notificationService: NotificationService
) {

    fun execute(token: String, topic: Topic) {
        notificationService.unsubscribeTopic(
            token = token,
            topic = topic
        )
    }
}

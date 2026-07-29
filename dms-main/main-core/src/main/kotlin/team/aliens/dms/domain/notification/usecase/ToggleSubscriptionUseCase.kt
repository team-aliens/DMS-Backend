package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.service.NotificationService

@UseCase
class ToggleSubscriptionUseCase(
    private val notificationService: NotificationService
) {

    fun execute(deviceToken: String, topic: Topic) {
        notificationService.toggleSubscription(
            token = deviceToken,
            topic = topic
        )
    }
}

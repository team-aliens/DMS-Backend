package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.service.NotificationService

@UseCase
class UpdateTopicSubscribesUseCase(
    private val notificationService: NotificationService
) {

    fun execute(deviceToken: String, topicsToSubscribe: List<Pair<Topic, Boolean>>) {
        notificationService.updateSubscribes(
            deviceToken = deviceToken,
            topicsToSubscribe = topicsToSubscribe
        )
    }
}

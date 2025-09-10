package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.contract.model.Topic
import team.aliens.dms.domain.notification.service.NotificationService

@UseCase
class UpdateTopicSubscriptionsUseCase(
    private val notificationService: NotificationService
) {

    fun execute(token: String, topicsToSubscribe: List<Pair<Topic, Boolean>>) {
        notificationService.updateSubscribes(
            token = token,
            topicsToSubscribe = topicsToSubscribe
        )
    }
}

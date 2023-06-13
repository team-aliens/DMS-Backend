package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class UpdateTopicSubscribesUseCase(
    private val userService: UserService,
    private val notificationService: NotificationService
) {

    fun execute(topicSubscribes: List<Pair<Topic, Boolean>>) {
        val user = userService.getCurrentUser()

        notificationService.updateSubscribes(
            userId = user.id,
            topicSubscribes = topicSubscribes
        )
    }
}

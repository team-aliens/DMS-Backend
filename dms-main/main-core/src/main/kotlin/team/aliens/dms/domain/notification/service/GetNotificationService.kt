package team.aliens.dms.domain.notification.service

import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.domain.notification.model.TopicSubscription
import java.util.UUID

interface GetNotificationService {

    fun getNotificationOfUsersByUserId(userId: UUID): List<NotificationOfUser>

    fun getTopicSubscriptionsByToken(token: String): List<TopicSubscription>

    fun getDeviceTokenByToken(token: String): DeviceToken
}

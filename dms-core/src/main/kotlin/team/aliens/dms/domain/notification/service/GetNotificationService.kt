package team.aliens.dms.domain.notification.service

import java.util.UUID
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.domain.notification.model.TopicSubscription

interface GetNotificationService {

    fun getNotificationOfUsersByUserId(userId: UUID): List<NotificationOfUser>

    fun getTopicSubscriptionsByToken(token: String): List<TopicSubscription>

    fun getDeviceTokenByToken(token: String): DeviceToken
}
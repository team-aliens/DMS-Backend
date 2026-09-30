package team.aliens.dms.domain.notification.service

import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.NotificationOfUser
import java.util.UUID

interface CommandNotificationService {

    fun saveDeviceToken(deviceToken: DeviceToken): DeviceToken

    fun deleteDeviceTokenByUserId(userId: UUID)

    fun deleteNotificationOfUserByUserIdAndId(userId: UUID, notificationOfUserId: UUID)

    fun deleteNotificationOfUserByUserId(userId: UUID)

    fun saveNotificationOfUser(notificationOfUser: NotificationOfUser): NotificationOfUser

    fun saveNotificationsOfUser(notificationOfUsers: List<NotificationOfUser>)

    fun deleteOldNotifications()

    fun subscribeTopic(token: String, topic: Topic)

    fun unsubscribeTopic(token: String, topic: Topic)

    fun updateSubscribes(token: String, topicsToSubscribe: List<Pair<Topic, Boolean>>)

    fun toggleSubscription(token: String, topic: Topic)

    fun sendMessage(
        deviceToken: DeviceToken,
        notification: Notification
    )

    fun sendMessages(deviceTokens: List<DeviceToken>, notification: Notification)

    fun sendMessagesByTopic(
        notification: Notification
    )
}

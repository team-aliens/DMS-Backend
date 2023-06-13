package team.aliens.dms.domain.notification.service

import java.util.UUID
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.Topic

interface NotificationService {

    fun saveDeviceToken(deviceToken: DeviceToken)

    fun sendNotificationByUserId(userId: UUID, notification: Notification)

    fun sendNotificationToAll(notification: Notification)

    fun unsubscribeTopic(userId: UUID, topic: Topic)

    fun subscribeTopic(userId: UUID, topic: Topic)

    fun updateSubscribes(userId: UUID, topicSubscribes: List<Pair<Topic, Boolean>>)
}

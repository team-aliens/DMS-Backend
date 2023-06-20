package team.aliens.dms.domain.notification.service

import java.util.UUID
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.domain.notification.spi.NotificationOfUserPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.user.spi.UserPort

@Component
class NotificationServiceImpl(
    private val notificationPort: NotificationPort,
    private val notificationOfUserPort: NotificationOfUserPort,
    private val userPort: UserPort,
    private val deviceTokenPort: DeviceTokenPort
) : NotificationService {

    override fun saveDeviceToken(deviceToken: DeviceToken) {
        deviceTokenPort.saveDeviceToken(deviceToken)
        notificationPort.subscribeAllTopics(
            deviceToken = deviceToken.deviceToken
        )
    }

    override fun subscribeTopic(deviceToken: String, topic: Topic) {
        notificationPort.subscribeTopic(
            deviceToken = deviceToken,
            topic = topic
        )
    }

    override fun unsubscribeTopic(deviceToken: String, topic: Topic) {
        notificationPort.unsubscribeTopic(
            deviceToken = deviceToken,
            topic = topic
        )
    }

    override fun updateSubscribes(deviceToken: String, topicsToSubscribe: List<Pair<Topic, Boolean>>) {

        topicsToSubscribe.forEach { (topic, isSubscribe) ->
            if (isSubscribe) {
                notificationPort.subscribeTopic(
                    deviceToken = deviceToken,
                    topic = topic
                )
            } else {
                notificationPort.unsubscribeTopic(
                    deviceToken = deviceToken,
                    topic = topic
                )
            }
        }
    }

    private fun getDeviceTokenByDeviceToken(deviceToken: String) =
        deviceTokenPort.queryDeviceTokenByDeviceToken(deviceToken) ?: throw DeviceTokenNotFoundException

    override fun sendMessage(deviceToken: DeviceToken, notification: Notification) {
        notification.runIfSaveRequired {
            notificationOfUserPort.saveNotificationOfUser(
                notification.toNotificationOfUser(deviceToken.userId)
            )
        }
        notificationPort.sendMessage(
            deviceToken = deviceToken.deviceToken,
            notification = notification
        )
    }

    override fun sendMessages(deviceTokens: List<DeviceToken>, notification: Notification) {
        notification.runIfSaveRequired {
            notificationOfUserPort.saveNotificationsOfUser(
                deviceTokens.map { notification.toNotificationOfUser(it.userId) }
            )
        }
        notificationPort.sendMessages(
            deviceTokens = deviceTokens.map { it.deviceToken },
            notification = notification
        )
    }

    override fun sendMessagesByTopic(notification: Notification) {
        notification.runIfSaveRequired {
            val users = userPort.queryUsersBySchoolId(notification.schoolId)
            notificationOfUserPort.saveNotificationsOfUser(
                users.map { notification.toNotificationOfUser(it.id) }
            )
        }
        notificationPort.sendByTopic(
            notification = notification
        )
    }

    override fun getNotificationOfUsersByUserId(userId: UUID) =
        notificationOfUserPort.queryNotificationOfUserByUserId(userId)
}

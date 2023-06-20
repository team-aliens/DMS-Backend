package team.aliens.dms.domain.notification.service

import java.util.UUID
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.model.TopicSubscription
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.domain.notification.spi.NotificationOfUserPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.user.spi.UserPort
import team.aliens.dms.domain.notification.spi.TopicSubscriptionPort

@Component
class NotificationServiceImpl(
    private val notificationPort: NotificationPort,
    private val notificationOfUserPort: NotificationOfUserPort,
    private val userPort: UserPort,
    private val topicSubscriptionPort: TopicSubscriptionPort,
    private val deviceTokenPort: DeviceTokenPort
) : NotificationService {

    override fun saveDeviceToken(deviceToken: DeviceToken) {
        deviceTokenPort.saveDeviceToken(deviceToken)
        notificationPort.subscribeAllTopics(
            token = deviceToken.token
        )
    }

    override fun subscribeTopic(token: String, topic: Topic) {
        val deviceToken = this.getDeviceTokenByToken(token)
        topicSubscriptionPort.saveTopicSubscription(
            TopicSubscription.subscribe(
                deviceTokenId = deviceToken.id,
                topic = topic,
            )
        )
        notificationPort.subscribeTopic(
            deviceToken = deviceToken,
            topic = topic
        )
    }

    override fun unsubscribeTopic(token: String, topic: Topic) {
        val deviceToken = this.getDeviceTokenByToken(token)
        topicSubscriptionPort.saveTopicSubscription(
            TopicSubscription.unsubscribe(
                deviceTokenId = deviceToken.id,
                topic = topic,
            )
        )
        notificationPort.unsubscribeTopic(
            deviceToken = deviceToken,
            topic = topic
        )
    }

    override fun updateSubscribes(token: String, topicsToSubscribe: List<Pair<Topic, Boolean>>) {
        val deviceToken = this.getDeviceTokenByToken(token)
        val topicSubscriptions = topicsToSubscribe.map { (topic, isSubscribe) ->
            this.subscribeOrUnsubscribeTopic(isSubscribe, token, topic)
            TopicSubscription(
                deviceTokenId = deviceToken.id,
                topic = topic,
                isSubscribed = isSubscribe
            )
        }
        topicSubscriptionPort.saveAllTopicSubscriptions(topicSubscriptions)
    }

    private fun subscribeOrUnsubscribeTopic(
        isSubscribe: Boolean,
        token: String,
        topic: Topic
    ) {
        if (isSubscribe) {
            notificationPort.subscribeTopic(
                token = token,
                topic = topic
            )
        } else {
            notificationPort.unsubscribeTopic(
                token = token,
                topic = topic
            )
        }
    }

    private fun getDeviceTokenByToken(token: String) =
        deviceTokenPort.queryDeviceTokenByToken(token) ?: throw DeviceTokenNotFoundException

    override fun sendMessage(deviceToken: DeviceToken, notification: Notification) {
        notification.runIfSaveRequired {
            notificationOfUserPort.saveNotificationOfUser(
                notification.toNotificationOfUser(deviceToken.userId)
            )
        }
        notificationPort.sendMessage(
            deviceToken = deviceToken.deviceToken,

    override fun sendMessage(token: String, notification: Notification) {
        notificationPort.sendMessage(
            token = token,
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
            deviceTokens = deviceTokens,
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

    override fun getTopicSubscriptionsByToken(token: String): List<TopicSubscription> {
        val savedToken = getDeviceTokenByToken(token)
        return topicSubscriptionPort.queryTopicSubscriptionsByDeviceTokenId(savedToken.id)
    }
}

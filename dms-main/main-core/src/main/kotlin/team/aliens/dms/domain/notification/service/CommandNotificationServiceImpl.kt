package team.aliens.dms.domain.notification.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.exception.NotificationOfUserNotFoundException
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.domain.notification.model.TopicSubscription
import team.aliens.dms.domain.notification.spi.CommandNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.notification.spi.QueryNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.TopicSubscriptionPort
import java.time.LocalDateTime
import java.util.UUID

@Service
class CommandNotificationServiceImpl(
    private val deviceTokenPort: DeviceTokenPort,
    private val notificationPort: NotificationPort,
    private val queryNotificationOfUserPort: QueryNotificationOfUserPort,
    private val commandNotificationOfUserPort: CommandNotificationOfUserPort,
    private val topicSubscriptionPort: TopicSubscriptionPort
) : CommandNotificationService {

    override fun saveDeviceToken(deviceToken: DeviceToken): DeviceToken {
        return deviceTokenPort.saveDeviceToken(deviceToken).also {
            notificationPort.subscribeAllTopics(
                token = deviceToken.token
            )
        }
    }

    override fun deleteDeviceTokenByUserId(userId: UUID) {
        val deviceToken = deviceTokenPort.queryDeviceTokenByUserId(userId) ?: return

        topicSubscriptionPort.deleteAllByDeviceTokenId(deviceToken.id)
        deviceTokenPort.deleteDeviceTokenByUserId(userId)
    }

    override fun deleteNotificationOfUserByUserIdAndId(userId: UUID, notificationOfUserId: UUID) {
        queryNotificationOfUserPort.queryNotificationOfUserById(notificationOfUserId).also {
            if (it == null || it.userId != userId) {
                throw NotificationOfUserNotFoundException
            }
        }
        commandNotificationOfUserPort.deleteNotificationOfUserById(notificationOfUserId)
    }

    override fun deleteNotificationOfUserByUserId(userId: UUID) {
        commandNotificationOfUserPort.deleteNotificationOfUserByUserId(userId)
    }

    override fun saveNotificationOfUser(notificationOfUser: NotificationOfUser): NotificationOfUser {
        return commandNotificationOfUserPort.saveNotificationOfUser(notificationOfUser)
    }

    override fun saveNotificationsOfUser(notificationOfUsers: List<NotificationOfUser>) {
        commandNotificationOfUserPort.saveNotificationsOfUser(notificationOfUsers)
    }

    override fun deleteOldNotifications() {
        val cutoffDate = LocalDateTime.now().minusDays(60)
        commandNotificationOfUserPort.deleteOldNotificationOfUsers(cutoffDate)
    }

    override fun subscribeTopic(token: String, topic: Topic) {
        val deviceToken = getDeviceTokenByToken(token)
        topicSubscriptionPort.saveTopicSubscription(
            TopicSubscription.subscribe(
                deviceTokenId = deviceToken.id,
                topic = topic,
            )
        )
        notificationPort.subscribeTopic(
            token = deviceToken.token,
            topic = topic
        )
    }

    override fun unsubscribeTopic(token: String, topic: Topic) {
        val deviceToken = getDeviceTokenByToken(token)
        topicSubscriptionPort.saveTopicSubscription(
            TopicSubscription.unsubscribe(
                deviceTokenId = deviceToken.id,
                topic = topic,
            )
        )
        notificationPort.unsubscribeTopic(
            token = deviceToken.token,
            topic = topic
        )
    }

    override fun updateSubscribes(token: String, topicsToSubscribe: List<Pair<Topic, Boolean>>) {
        val deviceToken = getDeviceTokenByToken(token)
        val topicSubscriptions = topicsToSubscribe.map { (topic, isSubscribe) ->
            subscribeOrUnsubscribeTopic(isSubscribe, token, topic)
            TopicSubscription(
                deviceTokenId = deviceToken.id,
                topic = topic,
                isSubscribed = isSubscribe
            )
        }
        topicSubscriptionPort.saveAllTopicSubscriptions(topicSubscriptions)
    }

    override fun toggleSubscription(token: String, topic: Topic) {
        val deviceToken = getDeviceTokenByToken(token)
        val currentSubscribe = topicSubscriptionPort.queryDeviceTokenIdAndTopic(deviceToken.id, topic)

        currentSubscribe?.let {
            if (it.isSubscribed) {
                notificationPort.unsubscribeTopic(token, topic)
            } else {
                notificationPort.subscribeTopic(token, topic)
            }

            topicSubscriptionPort.saveTopicSubscription(
                TopicSubscription(
                    deviceTokenId = deviceToken.id,
                    topic = topic,
                    isSubscribed = !it.isSubscribed
                )
            )
        }
    }

    override fun sendMessage(deviceToken: DeviceToken, notification: Notification) {
        notification.runIfSaveRequired {
            commandNotificationOfUserPort.saveNotificationOfUser(
                notification.toNotificationOfUser(deviceToken.userId)
            )
        }
        notificationPort.sendMessage(
            token = deviceToken.token,
            notification = notification
        )
    }

    override fun sendMessages(deviceTokens: List<DeviceToken>, notification: Notification) {
        if (deviceTokens.isEmpty()) {
            return
        }
        notification.runIfSaveRequired {
            commandNotificationOfUserPort.saveNotificationsOfUser(
                deviceTokens.map { notification.toNotificationOfUser(it.userId) }
            )
        }
        notificationPort.sendMessages(
            tokens = deviceTokens.map { it.token },
            notification = notification
        )
    }

    override fun sendMessagesByTopic(notification: Notification) {
        notification.runIfSaveRequired {
            val userIds = deviceTokenPort.queryDeviceTokensBySubscriptionTopicAndSchoolId(
                notification.topic,
                notification.schoolId
            ).map {
                it.userId
            }

            commandNotificationOfUserPort.saveNotificationsOfUser(
                userIds.map { notification.toNotificationOfUser(it) }
            )
        }
        notificationPort.sendByTopic(
            notification = notification
        )
    }

    private fun getDeviceTokenByToken(token: String) =
        deviceTokenPort.queryDeviceTokenByToken(token) ?: throw DeviceTokenNotFoundException

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
}

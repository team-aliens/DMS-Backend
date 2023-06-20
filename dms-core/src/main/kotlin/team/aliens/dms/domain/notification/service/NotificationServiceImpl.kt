package team.aliens.dms.domain.notification.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.model.TopicSubscription
import team.aliens.dms.domain.notification.spi.NotificationOfUserPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.notification.spi.TopicSubscriptionPort
import team.aliens.dms.domain.user.spi.UserPort

@Service
class NotificationServiceImpl(
    private val notificationPort: NotificationPort,
    private val notificationOfUserPort: NotificationOfUserPort,
    private val userPort: UserPort,
    private val topicSubscriptionPort: TopicSubscriptionPort,
    getNotificationService: GetNotificationService,
    commandNotificationService: CommandNotificationService
) : NotificationService,
    GetNotificationService by getNotificationService,
    CommandNotificationService by commandNotificationService{

    override fun subscribeTopic(token: String, topic: Topic) {
        val deviceToken = this.getDeviceTokenByToken(token)
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
        val deviceToken = this.getDeviceTokenByToken(token)
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

    override fun sendMessage(deviceToken: DeviceToken, notification: Notification) {
        notification.runIfSaveRequired {
            notificationOfUserPort.saveNotificationOfUser(
                notification.toNotificationOfUser(deviceToken.userId)
            )
        }
        notificationPort.sendMessage(
            token = deviceToken.token,
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
            tokens = deviceTokens.map { it.token },
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
}

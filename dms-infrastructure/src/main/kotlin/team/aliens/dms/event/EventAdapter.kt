package team.aliens.dms.event

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import team.aliens.dms.common.spi.EventPort
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification

@Component
class EventAdapter(
    private val eventPublisher: ApplicationEventPublisher
) : EventPort {

    override fun publishNotification(deviceToken: DeviceToken, notification: Notification) {
        eventPublisher.publishEvent(
            SingleNotificationEvent(
                deviceToken = deviceToken,
                notification = notification
            )
        )
    }

    override fun publishNotificationToAllByTopic(
        notification: Notification
    ) {
        eventPublisher.publishEvent(
            TopicNotificationEvent(
                notification = notification
            )
        )
    }

    override fun publishNotificationToApplicant(
        notification: Notification,
        deviceTokens: List<DeviceToken>
    ) {
        eventPublisher.publishEvent(
            GroupNotificationEvent(
                deviceTokens = deviceTokens,
                notification = notification
            )
        )
    }
}

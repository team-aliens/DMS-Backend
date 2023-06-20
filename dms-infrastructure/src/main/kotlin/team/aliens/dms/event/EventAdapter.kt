package team.aliens.dms.event

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import team.aliens.dms.common.spi.EventPort
import team.aliens.dms.domain.notification.model.Notification

@Component
class EventAdapter(
    private val eventPublisher: ApplicationEventPublisher
) : EventPort {

    override fun publishNotification(
        token: String,
        notification: Notification
    ) {
        eventPublisher.publishEvent(
            SingleNotificationEvent(
                token = token,
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
}

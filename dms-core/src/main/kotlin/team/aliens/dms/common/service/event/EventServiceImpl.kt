package team.aliens.dms.common.service.event

import org.springframework.stereotype.Component
import team.aliens.dms.common.spi.EventPort
import team.aliens.dms.domain.notification.model.Notification

@Component
class EventServiceImpl(
    private val eventPort: EventPort
) : EventService {

    override fun publishNotification(
        deviceToken: String,
        notification: Notification
    ) {
        eventPort.publishNotification(
            deviceToken = deviceToken,
            notification = notification
        )
    }

    override fun publishNotificationToAll(
        notification: Notification
    ) {
        eventPort.publishNotificationToAll(
            notification = notification
        )
    }
}

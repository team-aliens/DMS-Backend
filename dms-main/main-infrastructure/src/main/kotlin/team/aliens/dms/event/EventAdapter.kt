package team.aliens.dms.event

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import team.aliens.dms.common.spi.EventPort
import team.aliens.dms.contract.model.notification.DeviceTokenInfo
import team.aliens.dms.contract.model.notification.NotificationInfo
import java.util.UUID

@Component
class EventAdapter(
    private val eventPublisher: ApplicationEventPublisher
) : EventPort {

    override fun publishNotification(userId: UUID, notificationInfo: NotificationInfo) {
        eventPublisher.publishEvent(
            SingleNotificationEvent(
                userId = userId,
                notificationInfo = notificationInfo
            )
        )
    }

    override fun publishNotificationToApplicant(userIds: List<UUID>, notificationInfo: NotificationInfo) {
        eventPublisher.publishEvent(
            GroupNotificationEvent(
                userIds = userIds,
                notificationInfo = notificationInfo
            )
        )
    }

    override fun publishSaveDeviceToken(deviceTokenInfo: DeviceTokenInfo) {
        eventPublisher.publishEvent(
            SaveDeviceTokenEvent(
                deviceTokenInfo = deviceTokenInfo
            )
        )
    }

    override fun publishDeleteDeviceToken(userId: UUID) {
        eventPublisher.publishEvent(
            DeleteDeviceTokenEvent(
                userId = userId
            )
        )
    }
}
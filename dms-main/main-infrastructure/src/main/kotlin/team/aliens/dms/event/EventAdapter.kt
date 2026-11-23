package team.aliens.dms.event

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import team.aliens.dms.common.spi.EventPort
import team.aliens.dms.contract.model.notification.DeviceTokenInfo
import team.aliens.dms.contract.model.notification.NotificationInfo
import team.aliens.dms.domain.notification.model.NotificationOutbox
import team.aliens.dms.domain.notification.spi.CommandNotificationOutboxPort
import java.util.UUID

@Component
class EventAdapter(
    private val eventPublisher: ApplicationEventPublisher,
    private val commandNotificationOutboxPort: CommandNotificationOutboxPort
) : EventPort {

    // 알림 발송 요청은 상태 변경과 같은 트랜잭션에 outbox 로 남긴다.
    // 트랜잭션 밖에서 불리면 커밋과 묶이지 않으므로 MANDATORY 로 바로 실패시킨다
    @Transactional(propagation = Propagation.MANDATORY)
    override fun publishNotification(userIds: List<UUID>, notificationInfo: NotificationInfo) {
        if (userIds.isEmpty()) return

        val outbox = commandNotificationOutboxPort.saveNotificationOutbox(
            NotificationOutbox(
                userIds = userIds,
                notificationInfo = notificationInfo
            )
        )
        eventPublisher.publishEvent(NotificationOutboxSavedEvent(outbox.id))
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

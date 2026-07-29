package team.aliens.dms.event.handler

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.event.DeleteDeviceTokenEvent
import team.aliens.dms.event.DeviceTokenEvent
import team.aliens.dms.event.SaveDeviceTokenEvent

@Component
class DeviceTokenEventHandler(
    private val notificationService: NotificationService
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleDeviceToken(event: DeviceTokenEvent) {
        when (event) {
            is SaveDeviceTokenEvent -> notificationService.saveDeviceToken(
                DeviceToken.from(event.deviceTokenInfo)
            )

            is DeleteDeviceTokenEvent -> notificationService.deleteDeviceTokenByUserId(event.userId)
        }
    }
}

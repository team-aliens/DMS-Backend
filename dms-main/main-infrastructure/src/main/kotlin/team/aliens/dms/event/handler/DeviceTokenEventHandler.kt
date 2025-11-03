package team.aliens.dms.event.handler

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.DeleteDeviceTokenMessage
import team.aliens.dms.SaveDeviceTokenMessage
import team.aliens.dms.event.DeleteDeviceTokenEvent
import team.aliens.dms.event.SaveDeviceTokenEvent
import team.aliens.dms.thirdparty.messagebroker.NotificationProducer

@Component
class DeviceTokenEventHandler(
    private val notificationProducer: NotificationProducer
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onSaveDeviceTokenEvent(event: SaveDeviceTokenEvent) {
        notificationProducer.sendMessage(
            SaveDeviceTokenMessage(
                deviceTokenInfo = event.deviceTokenInfo
            )
        )
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onDeleteDeviceTokenEvent(event: DeleteDeviceTokenEvent) {
        notificationProducer.sendMessage(
            DeleteDeviceTokenMessage(
                userId = event.userId
            )
        )
    }
}
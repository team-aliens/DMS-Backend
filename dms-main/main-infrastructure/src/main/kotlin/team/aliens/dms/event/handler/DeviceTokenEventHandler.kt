package team.aliens.dms.event.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.common.dto.OutboxData
import team.aliens.dms.common.dto.OutboxStatus
import team.aliens.dms.common.spi.OutboxPort
import team.aliens.dms.contract.remote.rabbitmq.DeleteDeviceTokenMessage
import team.aliens.dms.contract.remote.rabbitmq.SaveDeviceTokenMessage
import team.aliens.dms.event.DeleteDeviceTokenEvent
import team.aliens.dms.event.DeviceTokenEvent
import team.aliens.dms.event.SaveDeviceTokenEvent

@Component
class DeviceTokenEventHandler(
    private val outboxPort: OutboxPort,
    private val objectMapper: ObjectMapper
) {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun saveOutbox(event: DeviceTokenEvent) {
        val (eventType, message) = createMessage(event)

        outboxPort.save(
            OutboxData(
                id = null,
                aggregateType = "device_token",
                eventType = eventType,
                payload = objectMapper.writeValueAsString(message),
                status = OutboxStatus.PENDING
            )
        )
    }

    private fun createMessage(event: DeviceTokenEvent): Pair<String, Any> {
        return when (event) {
            is SaveDeviceTokenEvent -> "SaveDeviceTokenMessage" to SaveDeviceTokenMessage(
                deviceTokenInfo = event.deviceTokenInfo
            )
            is DeleteDeviceTokenEvent -> "DeleteDeviceTokenMessage" to DeleteDeviceTokenMessage(
                userId = event.userId
            )
        }
    }
}

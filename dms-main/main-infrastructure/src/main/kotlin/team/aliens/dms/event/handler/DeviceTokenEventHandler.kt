package team.aliens.dms.event.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
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
import team.aliens.dms.thirdparty.messagebroker.NotificationProducer
import java.util.UUID

@Component
class DeviceTokenEventHandler(
    private val outboxPort: OutboxPort,
    private val notificationProducer: NotificationProducer,
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val outboxIdHolder = ThreadLocal<UUID>()

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun saveOutbox(event: DeviceTokenEvent) {
        val (eventType, message) = createMessage(event)

        val saved = outboxPort.save(
            OutboxData(
                id = null,
                aggregateType = "device_token",
                eventType = eventType,
                payload = objectMapper.writeValueAsString(message),
                status = OutboxStatus.PENDING
            )
        )
        outboxIdHolder.set(saved.id)
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun publishMessage(event: DeviceTokenEvent) {
        val (_, message) = createMessage(event)
        val outboxId = outboxIdHolder.get()

        try {
            notificationProducer.sendMessage(message)
            outboxId?.let { outboxPort.deleteById(it) }
        } catch (e: Exception) {
            log.warn("Failed to send device token message immediately, will be retried by scheduler", e)
        } finally {
            outboxIdHolder.remove()
        }
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

package team.aliens.dms.event.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.contract.remote.rabbitmq.DeleteDeviceTokenMessage
import team.aliens.dms.contract.remote.rabbitmq.SaveDeviceTokenMessage
import team.aliens.dms.event.DeleteDeviceTokenEvent
import team.aliens.dms.event.DeviceTokenEvent
import team.aliens.dms.event.SaveDeviceTokenEvent
import team.aliens.dms.thirdparty.messagebroker.NotificationProducer
import team.aliens.dms.persistence.outbox.entity.OutboxJpaEntity
import team.aliens.dms.persistence.outbox.entity.OutboxStatus
import team.aliens.dms.persistence.outbox.repository.OutboxJpaRepository

@Component
class DeviceTokenEventHandler(
    private val outboxRepository: OutboxJpaRepository,
    private val notificationProducer: NotificationProducer,
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun saveOutbox(event: DeviceTokenEvent) {
        val (eventType, message) = createMessage(event)

        outboxRepository.save(
            OutboxJpaEntity(
                aggregateType = "device_token",
                eventType = eventType,
                payload = objectMapper.writeValueAsString(message),
                status = OutboxStatus.PENDING
            )
        )
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun publishMessage(event: DeviceTokenEvent) {
        val (_, message) = createMessage(event)

        try {
            notificationProducer.sendMessage(message)
            outboxRepository.findByStatusAndPayload(
                OutboxStatus.PENDING,
                objectMapper.writeValueAsString(message)
            )?.let { outboxRepository.delete(it) }
        } catch (e: Exception) {
            log.warn("Failed to send device token message immediately, will be retried by scheduler", e)
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

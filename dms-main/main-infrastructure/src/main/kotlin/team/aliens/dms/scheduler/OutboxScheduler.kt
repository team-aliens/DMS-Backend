package team.aliens.dms.scheduler

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import team.aliens.dms.common.dto.OutboxData
import team.aliens.dms.common.dto.OutboxStatus
import team.aliens.dms.common.spi.OutboxPort
import team.aliens.dms.contract.remote.rabbitmq.DeleteDeviceTokenMessage
import team.aliens.dms.contract.remote.rabbitmq.GroupNotificationMessage
import team.aliens.dms.contract.remote.rabbitmq.SaveDeviceTokenMessage
import team.aliens.dms.contract.remote.rabbitmq.SingleNotificationMessage
import team.aliens.dms.contract.remote.rabbitmq.TopicNotificationMessage
import team.aliens.dms.scheduler.error.exception.UnknownEventTypeException
import team.aliens.dms.thirdparty.messagebroker.NotificationProducer

@Component
class OutboxScheduler(
    private val outboxPort: OutboxPort,
    private val notificationProducer: NotificationProducer,
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    companion object {
        private const val MAX_RETRY_COUNT = 3
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    fun processOutbox() {
        val pendingOutboxes = outboxPort.findByStatus(OutboxStatus.PENDING)

        pendingOutboxes.forEach { outbox ->
            try {
                val message = deserializeMessage(outbox)
                notificationProducer.sendMessage(message)
                outboxPort.delete(outbox)
            } catch (e: Exception) {
                log.error("Failed to process outbox: ${outbox.id}", e)
                handleFailure(outbox)
            }
        }
    }

    private fun deserializeMessage(outbox: OutboxData): Any {
        val messageClass = when (outbox.eventType) {
            SingleNotificationMessage.TYPE -> SingleNotificationMessage::class.java
            GroupNotificationMessage.TYPE -> GroupNotificationMessage::class.java
            TopicNotificationMessage.TYPE -> TopicNotificationMessage::class.java
            SaveDeviceTokenMessage.TYPE -> SaveDeviceTokenMessage::class.java
            DeleteDeviceTokenMessage.TYPE -> DeleteDeviceTokenMessage::class.java
            else -> {
                log.error("Unknown event type: ${outbox.eventType}")
                throw UnknownEventTypeException
            }
        }
        return objectMapper.readValue(outbox.payload, messageClass)
    }

    private fun handleFailure(outbox: OutboxData) {
        val newRetryCount = outbox.retryCount + 1
        if (newRetryCount < MAX_RETRY_COUNT) {
            outboxPort.save(
                outbox.copy(
                    status = OutboxStatus.PENDING,
                    retryCount = newRetryCount
                )
            )
        } else {
            outboxPort.save(
                outbox.copy(
                    status = OutboxStatus.FAILED,
                    retryCount = newRetryCount
                )
            )
            log.error("Outbox message exceeded max retry count: ${outbox.id}")
        }
    }
}

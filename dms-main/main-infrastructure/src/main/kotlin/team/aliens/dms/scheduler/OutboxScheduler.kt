package team.aliens.dms.scheduler

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import team.aliens.dms.contract.remote.rabbitmq.DeleteDeviceTokenMessage
import team.aliens.dms.contract.remote.rabbitmq.GroupNotificationMessage
import team.aliens.dms.contract.remote.rabbitmq.SaveDeviceTokenMessage
import team.aliens.dms.contract.remote.rabbitmq.SingleNotificationMessage
import team.aliens.dms.contract.remote.rabbitmq.TopicNotificationMessage
import team.aliens.dms.scheduler.error.exception.UnknownEventTypeException
import team.aliens.dms.thirdparty.messagebroker.NotificationProducer
import team.aliens.dms.persistence.outbox.entity.OutboxJpaEntity
import team.aliens.dms.persistence.outbox.entity.OutboxStatus
import team.aliens.dms.persistence.outbox.repository.OutboxJpaRepository

@Component
class OutboxScheduler(
    private val outboxRepository: OutboxJpaRepository,
    private val notificationProducer: NotificationProducer,
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    companion object {
        private const val MAX_RETRY_COUNT = 3
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    fun processOutbox() {
        val pendingOutboxes = outboxRepository.findByStatus(OutboxStatus.PENDING)

        pendingOutboxes.forEach { outbox ->
            try {
                val message = deserializeMessage(outbox)
                notificationProducer.sendMessage(message)
                outboxRepository.delete(outbox)
            } catch (e: Exception) {
                log.error("Failed to process outbox: ${outbox.id}", e)
                handleFailure(outbox)
            }
        }
    }

    private fun deserializeMessage(outbox: OutboxJpaEntity): Any {
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

    private fun handleFailure(outbox: OutboxJpaEntity) {
        val newRetryCount = outbox.retryCount + 1
        if (newRetryCount < MAX_RETRY_COUNT) {
            outboxRepository.save(
                OutboxJpaEntity(
                    id = outbox.id,
                    aggregateType = outbox.aggregateType,
                    eventType = outbox.eventType,
                    payload = outbox.payload,
                    status = OutboxStatus.PENDING,
                    retryCount = newRetryCount,
                    createdAt = outbox.createdAt
                )
            )
        } else {
            outboxRepository.save(
                OutboxJpaEntity(
                    id = outbox.id,
                    aggregateType = outbox.aggregateType,
                    eventType = outbox.eventType,
                    payload = outbox.payload,
                    status = OutboxStatus.FAILED,
                    retryCount = newRetryCount,
                    createdAt = outbox.createdAt
                )
            )
            log.error("Outbox message exceeded max retry count: ${outbox.id}")
        }
    }
}

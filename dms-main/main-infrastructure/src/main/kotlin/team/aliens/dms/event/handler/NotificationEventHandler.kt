package team.aliens.dms.event.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.common.dto.OutboxData
import team.aliens.dms.common.dto.OutboxStatus
import team.aliens.dms.common.spi.OutboxPort
import team.aliens.dms.contract.remote.rabbitmq.GroupNotificationMessage
import team.aliens.dms.contract.remote.rabbitmq.SingleNotificationMessage
import team.aliens.dms.contract.remote.rabbitmq.TopicNotificationMessage
import team.aliens.dms.event.GroupNotificationEvent
import team.aliens.dms.event.NotificationEvent
import team.aliens.dms.event.SingleNotificationEvent
import team.aliens.dms.event.TopicNotificationEvent
import team.aliens.dms.thirdparty.messagebroker.NotificationProducer
import java.util.UUID

@Component
class NotificationEventHandler(
    private val outboxPort: OutboxPort,
    private val notificationProducer: NotificationProducer,
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val outboxIdsByEventIdentity = ThreadLocal<MutableMap<Int, UUID>>()

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun saveOutbox(event: NotificationEvent) {
        val (eventType, message) = createMessage(event)

        val saved = outboxPort.save(
            OutboxData(
                id = null,
                aggregateType = "notification",
                eventType = eventType,
                payload = objectMapper.writeValueAsString(message),
                status = OutboxStatus.PENDING
            )
        )

        val map = outboxIdsByEventIdentity.get() ?: mutableMapOf<Int, UUID>().also {
            outboxIdsByEventIdentity.set(it)
        }

        map[System.identityHashCode(event)] = saved.id!!
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun publishMessage(event: NotificationEvent) {
        val (_, message) = createMessage(event)
        val map = outboxIdsByEventIdentity.get()
        val outboxId = map.remove(System.identityHashCode(event))

        try {
            notificationProducer.sendMessage(message)
            outboxId?.let { outboxPort.deleteById(it) }
        } catch (e: Exception) {
            log.warn("Failed to send notification immediately, will be retried by scheduler", e)
        } finally {
            if (map.isNullOrEmpty()) {
                outboxIdsByEventIdentity.remove()
            }
        }
    }

    private fun createMessage(event: NotificationEvent): Pair<String, Any> {
        return when (event) {
            is SingleNotificationEvent -> SingleNotificationMessage.TYPE to SingleNotificationMessage(
                userId = event.userId,
                notificationInfo = event.notificationInfo
            )
            is GroupNotificationEvent -> GroupNotificationMessage.TYPE to GroupNotificationMessage(
                userIds = event.userIds,
                notificationInfo = event.notificationInfo
            )
            is TopicNotificationEvent -> TopicNotificationMessage.TYPE to TopicNotificationMessage(
                notificationInfo = event.notificationInfo
            )
        }
    }
}

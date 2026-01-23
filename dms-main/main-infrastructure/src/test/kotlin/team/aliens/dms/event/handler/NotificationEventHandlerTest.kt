package team.aliens.dms.event.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.common.dto.OutboxData
import team.aliens.dms.common.dto.OutboxStatus
import team.aliens.dms.common.spi.OutboxPort
import team.aliens.dms.contract.model.notification.NotificationInfo
import team.aliens.dms.contract.remote.rabbitmq.SingleNotificationMessage
import team.aliens.dms.event.SingleNotificationEvent
import java.util.UUID

class NotificationEventHandlerTest : DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val outboxPort = mockk<OutboxPort>()
    val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
    val notificationEventHandler = NotificationEventHandler(outboxPort, objectMapper)

    describe("saveOutbox") {
        context("SingleNotificationEvent가 발생하면") {
            val userId = UUID.randomUUID()
            val notificationInfo = mockk<NotificationInfo>(relaxed = true)
            val event = SingleNotificationEvent(
                userId = userId,
                notificationInfo = notificationInfo
            )
            val savedOutbox = OutboxData(
                id = UUID.randomUUID(),
                aggregateType = "notification",
                eventType = SingleNotificationMessage.TYPE,
                payload = objectMapper.writeValueAsString(
                    SingleNotificationMessage(userId, notificationInfo)
                ),
                status = OutboxStatus.PENDING,
                retryCount = 0
            )

            every { outboxPort.save(any()) } returns savedOutbox

            it("Outbox에 이벤트를 저장한다") {
                notificationEventHandler.saveOutbox(event)

                verify {
                    outboxPort.save(
                        withArg {
                            assert(it.aggregateType == "notification")
                            assert(it.eventType == SingleNotificationMessage.TYPE)
                            assert(it.status == OutboxStatus.PENDING)
                        }
                    )
                }
            }
        }
    }
})

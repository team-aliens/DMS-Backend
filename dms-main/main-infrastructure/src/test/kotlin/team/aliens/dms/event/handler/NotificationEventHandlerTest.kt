package team.aliens.dms.event.handler

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.common.dto.OutboxData
import team.aliens.dms.common.dto.OutboxStatus
import team.aliens.dms.common.spi.OutboxPort
import team.aliens.dms.contract.model.notification.NotificationInfo
import team.aliens.dms.contract.remote.rabbitmq.SingleNotificationMessage
import team.aliens.dms.event.SingleNotificationEvent
import team.aliens.dms.thirdparty.messagebroker.NotificationProducer
import java.util.UUID

class NotificationEventHandlerTest : DescribeSpec({

    val outboxPort = mockk<OutboxPort>()
    val notificationProducer = mockk<NotificationProducer>()
    val objectMapper = ObjectMapper()
    val notificationEventHandler = NotificationEventHandler(outboxPort, notificationProducer, objectMapper)

    describe("saveOutbox") {
        context("SingleNotificationEvent가 발생하면") {
            val userId = UUID.randomUUID()
            val notificationInfo = mockk<NotificationInfo>()
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

    describe("publishMessage") {
        context("메시지 전송에 성공하면") {
            val userId = UUID.randomUUID()
            val notificationInfo = mockk<NotificationInfo>()
            val event = SingleNotificationEvent(
                userId = userId,
                notificationInfo = notificationInfo
            )
            val outboxId = UUID.randomUUID()
            val savedOutbox = OutboxData(
                id = outboxId,
                aggregateType = "notification",
                eventType = SingleNotificationMessage.TYPE,
                payload = objectMapper.writeValueAsString(
                    SingleNotificationMessage(userId, notificationInfo)
                ),
                status = OutboxStatus.PENDING,
                retryCount = 0
            )

            every { outboxPort.save(any()) } returns savedOutbox
            every { notificationProducer.sendMessage(any()) } just Runs
            every { outboxPort.deleteById(outboxId) } just Runs

            it("메시지를 전송하고 Outbox를 삭제한다") {
                notificationEventHandler.saveOutbox(event)
                notificationEventHandler.publishMessage(event)

                verify { notificationProducer.sendMessage(any()) }
                verify { outboxPort.deleteById(outboxId) }
            }
        }

        context("메시지 전송에 실패하면") {
            val userId = UUID.randomUUID()
            val notificationInfo = mockk<NotificationInfo>()
            val event = SingleNotificationEvent(
                userId = userId,
                notificationInfo = notificationInfo
            )
            val outboxId = UUID.randomUUID()
            val savedOutbox = OutboxData(
                id = outboxId,
                aggregateType = "notification",
                eventType = SingleNotificationMessage.TYPE,
                payload = objectMapper.writeValueAsString(
                    SingleNotificationMessage(userId, notificationInfo)
                ),
                status = OutboxStatus.PENDING,
                retryCount = 0
            )

            every { outboxPort.save(any()) } returns savedOutbox
            every { notificationProducer.sendMessage(any()) } throws RuntimeException("Send failed")

            it("Outbox를 삭제하지 않고 스케줄러가 재시도하도록 남겨둔다") {
                notificationEventHandler.saveOutbox(event)
                notificationEventHandler.publishMessage(event)

                verify { notificationProducer.sendMessage(any()) }
                verify(exactly = 0) { outboxPort.deleteById(any()) }
            }
        }
    }
})

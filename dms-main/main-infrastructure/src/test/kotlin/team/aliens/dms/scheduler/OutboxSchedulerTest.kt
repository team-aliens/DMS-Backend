package team.aliens.dms.scheduler

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.throwables.shouldThrow
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
import team.aliens.dms.scheduler.error.exception.UnknownEventTypeException
import team.aliens.dms.thirdparty.messagebroker.NotificationProducer
import java.time.LocalDateTime
import java.util.UUID

class OutboxSchedulerTest : DescribeSpec({

    val outboxPort = mockk<OutboxPort>()
    val notificationProducer = mockk<NotificationProducer>()
    val objectMapper = ObjectMapper()
    val outboxScheduler = OutboxScheduler(outboxPort, notificationProducer, objectMapper)

    describe("processOutbox") {
        context("PENDING 상태의 Outbox가 있고 메시지 전송에 성공하면") {
            val outboxId = UUID.randomUUID()
            val notificationInfo = mockk<NotificationInfo>()
            val message = SingleNotificationMessage(
                userId = UUID.randomUUID(),
                notificationInfo = notificationInfo
            )
            val payload = objectMapper.writeValueAsString(message)
            val outbox = OutboxData(
                id = outboxId,
                aggregateType = "notification",
                eventType = SingleNotificationMessage.TYPE,
                payload = payload,
                status = OutboxStatus.PENDING,
                retryCount = 0,
                createdAt = LocalDateTime.now()
            )

            every { outboxPort.findByStatus(OutboxStatus.PENDING) } returns listOf(outbox)
            every { notificationProducer.sendMessage(any()) } just Runs
            every { outboxPort.delete(outbox) } just Runs

            it("메시지를 전송하고 Outbox를 삭제한다") {
                outboxScheduler.processOutbox()

                verify { notificationProducer.sendMessage(any()) }
                verify { outboxPort.delete(outbox) }
            }
        }

        context("PENDING 상태의 Outbox가 있고 메시지 전송에 실패하면") {
            val outboxId = UUID.randomUUID()
            val notificationInfo = mockk<NotificationInfo>()
            val message = SingleNotificationMessage(
                userId = UUID.randomUUID(),
                notificationInfo = notificationInfo
            )
            val payload = objectMapper.writeValueAsString(message)
            val outbox = OutboxData(
                id = outboxId,
                aggregateType = "notification",
                eventType = SingleNotificationMessage.TYPE,
                payload = payload,
                status = OutboxStatus.PENDING,
                retryCount = 0,
                createdAt = LocalDateTime.now()
            )

            every { outboxPort.findByStatus(OutboxStatus.PENDING) } returns listOf(outbox)
            every { notificationProducer.sendMessage(any()) } throws RuntimeException("Send failed")
            every { outboxPort.save(any()) } returns outbox.copy(retryCount = 1)

            it("재시도 횟수를 증가시키고 PENDING 상태로 저장한다") {
                outboxScheduler.processOutbox()

                verify {
                    outboxPort.save(
                        withArg {
                            assert(it.retryCount == 1)
                            assert(it.status == OutboxStatus.PENDING)
                        }
                    )
                }
            }
        }

        context("재시도 횟수가 최대치를 초과하면") {
            val outboxId = UUID.randomUUID()
            val notificationInfo = mockk<NotificationInfo>()
            val message = SingleNotificationMessage(
                userId = UUID.randomUUID(),
                notificationInfo = notificationInfo
            )
            val payload = objectMapper.writeValueAsString(message)
            val outbox = OutboxData(
                id = outboxId,
                aggregateType = "notification",
                eventType = SingleNotificationMessage.TYPE,
                payload = payload,
                status = OutboxStatus.PENDING,
                retryCount = 2,
                createdAt = LocalDateTime.now()
            )

            every { outboxPort.findByStatus(OutboxStatus.PENDING) } returns listOf(outbox)
            every { notificationProducer.sendMessage(any()) } throws RuntimeException("Send failed")
            every { outboxPort.save(any()) } returns outbox.copy(retryCount = 3, status = OutboxStatus.FAILED)

            it("FAILED 상태로 저장한다") {
                outboxScheduler.processOutbox()

                verify {
                    outboxPort.save(
                        withArg {
                            assert(it.retryCount == 3)
                            assert(it.status == OutboxStatus.FAILED)
                        }
                    )
                }
            }
        }

        context("알 수 없는 이벤트 타입이면") {
            val outbox = OutboxData(
                id = UUID.randomUUID(),
                aggregateType = "notification",
                eventType = "UnknownType",
                payload = "{}",
                status = OutboxStatus.PENDING,
                retryCount = 0,
                createdAt = LocalDateTime.now()
            )

            every { outboxPort.findByStatus(OutboxStatus.PENDING) } returns listOf(outbox)
            every { outboxPort.save(any()) } returns outbox.copy(retryCount = 1)

            it("UnknownEventTypeException이 발생하고 재시도 횟수를 증가시킨다") {
                outboxScheduler.processOutbox()

                verify { outboxPort.save(any()) }
            }
        }
    }
})

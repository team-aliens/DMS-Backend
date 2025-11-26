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
import team.aliens.dms.contract.model.notification.DeviceTokenInfo
import team.aliens.dms.contract.remote.rabbitmq.SaveDeviceTokenMessage
import team.aliens.dms.event.SaveDeviceTokenEvent
import team.aliens.dms.thirdparty.messagebroker.NotificationProducer
import java.util.UUID

class DeviceTokenEventHandlerTest : DescribeSpec({

    val outboxPort = mockk<OutboxPort>()
    val notificationProducer = mockk<NotificationProducer>()
    val objectMapper = ObjectMapper()
    val deviceTokenEventHandler = DeviceTokenEventHandler(outboxPort, notificationProducer, objectMapper)

    describe("saveOutbox") {
        context("SaveDeviceTokenEvent가 발생하면") {
            val userId = UUID.randomUUID()
            val deviceTokenInfo = DeviceTokenInfo(
                userId = userId,
                schoolId = UUID.randomUUID(),
                token = "test-fcm-token"
            )
            val event = SaveDeviceTokenEvent(deviceTokenInfo = deviceTokenInfo)
            val savedOutbox = OutboxData(
                id = UUID.randomUUID(),
                aggregateType = "device_token",
                eventType = "SaveDeviceTokenMessage",
                payload = objectMapper.writeValueAsString(SaveDeviceTokenMessage(deviceTokenInfo)),
                status = OutboxStatus.PENDING,
                retryCount = 0
            )

            every { outboxPort.save(any()) } returns savedOutbox

            it("Outbox에 이벤트를 저장한다") {
                deviceTokenEventHandler.saveOutbox(event)

                verify {
                    outboxPort.save(
                        withArg {
                            assert(it.aggregateType == "device_token")
                            assert(it.eventType == "SaveDeviceTokenMessage")
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
            val deviceTokenInfo = DeviceTokenInfo(
                userId = userId,
                schoolId = UUID.randomUUID(),
                token = "test-fcm-token"
            )
            val event = SaveDeviceTokenEvent(deviceTokenInfo = deviceTokenInfo)
            val outboxId = UUID.randomUUID()
            val savedOutbox = OutboxData(
                id = outboxId,
                aggregateType = "device_token",
                eventType = "SaveDeviceTokenMessage",
                payload = objectMapper.writeValueAsString(SaveDeviceTokenMessage(deviceTokenInfo)),
                status = OutboxStatus.PENDING,
                retryCount = 0
            )

            every { outboxPort.save(any()) } returns savedOutbox
            every { notificationProducer.sendMessage(any()) } just Runs
            every { outboxPort.deleteById(outboxId) } just Runs

            it("메시지를 전송하고 Outbox를 삭제한다") {
                deviceTokenEventHandler.saveOutbox(event)
                deviceTokenEventHandler.publishMessage(event)

                verify { notificationProducer.sendMessage(any()) }
                verify { outboxPort.deleteById(outboxId) }
            }
        }

        context("메시지 전송에 실패하면") {
            val userId = UUID.randomUUID()
            val deviceTokenInfo = DeviceTokenInfo(
                userId = userId,
                schoolId = UUID.randomUUID(),
                token = "test-fcm-token"
            )
            val event = SaveDeviceTokenEvent(deviceTokenInfo = deviceTokenInfo)
            val outboxId = UUID.randomUUID()
            val savedOutbox = OutboxData(
                id = outboxId,
                aggregateType = "device_token",
                eventType = "SaveDeviceTokenMessage",
                payload = objectMapper.writeValueAsString(SaveDeviceTokenMessage(deviceTokenInfo)),
                status = OutboxStatus.PENDING,
                retryCount = 0
            )

            every { outboxPort.save(any()) } returns savedOutbox
            every { notificationProducer.sendMessage(any()) } throws RuntimeException("Send failed")

            it("Outbox를 삭제하지 않고 스케줄러가 재시도하도록 남겨둔다") {
                deviceTokenEventHandler.saveOutbox(event)
                deviceTokenEventHandler.publishMessage(event)

                verify { notificationProducer.sendMessage(any()) }
                verify(exactly = 0) { outboxPort.deleteById(any()) }
            }
        }
    }
})

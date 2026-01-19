package team.aliens.dms.event.handler

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.common.dto.OutboxData
import team.aliens.dms.common.dto.OutboxStatus
import team.aliens.dms.common.spi.OutboxPort
import team.aliens.dms.contract.model.notification.DeviceTokenInfo
import team.aliens.dms.contract.remote.rabbitmq.SaveDeviceTokenMessage
import team.aliens.dms.event.SaveDeviceTokenEvent
import java.util.UUID

class DeviceTokenEventHandlerTest : DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val outboxPort = mockk<OutboxPort>()
    val objectMapper = ObjectMapper()
    val deviceTokenEventHandler = DeviceTokenEventHandler(outboxPort, objectMapper)

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
})

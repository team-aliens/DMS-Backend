package team.aliens.dms.event.handler

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.contract.model.notification.DeviceTokenInfo
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.event.DeleteDeviceTokenEvent
import team.aliens.dms.event.SaveDeviceTokenEvent
import java.util.UUID

class DeviceTokenEventHandlerTest : DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val notificationService = mockk<NotificationService>(relaxed = true)
    val deviceTokenEventHandler = DeviceTokenEventHandler(notificationService)

    describe("handleDeviceToken") {
        context("SaveDeviceTokenEvent가 발생하면") {
            val userId = UUID.randomUUID()
            val deviceTokenInfo = DeviceTokenInfo(
                userId = userId,
                schoolId = UUID.randomUUID(),
                token = "test-fcm-token"
            )
            val event = SaveDeviceTokenEvent(deviceTokenInfo = deviceTokenInfo)

            it("디바이스 토큰을 저장한다") {
                deviceTokenEventHandler.handleDeviceToken(event)

                verify {
                    notificationService.saveDeviceToken(any<DeviceToken>())
                }
            }
        }

        context("DeleteDeviceTokenEvent가 발생하면") {
            val userId = UUID.randomUUID()
            val event = DeleteDeviceTokenEvent(userId = userId)

            it("해당 유저의 디바이스 토큰을 삭제한다") {
                deviceTokenEventHandler.handleDeviceToken(event)

                verify {
                    notificationService.deleteDeviceTokenByUserId(userId)
                }
            }
        }
    }
})

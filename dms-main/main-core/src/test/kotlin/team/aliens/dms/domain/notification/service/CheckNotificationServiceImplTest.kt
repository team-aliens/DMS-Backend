package team.aliens.dms.domain.notification.service

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import java.util.UUID

class CheckNotificationServiceImplTest : DescribeSpec({

    val deviceTokenPort = mockk<QueryDeviceTokenPort>()

    val service = CheckNotificationServiceImpl(
        deviceTokenPort = deviceTokenPort
    )

    describe("checkDeviceTokenByUserId") {
        context("디바이스 토큰이 있는 사용자 ID로 확인하면") {
            val userId = UUID.randomUUID()

            every { deviceTokenPort.existsDeviceTokenByUserId(userId) } returns true

            val result = service.checkDeviceTokenByUserId(userId)

            it("true를 반환한다") {
                result shouldBe true
            }
        }

        context("디바이스 토큰이 없는 사용자 ID로 확인하면") {
            val userId = UUID.randomUUID()

            every { deviceTokenPort.existsDeviceTokenByUserId(userId) } returns false

            val result = service.checkDeviceTokenByUserId(userId)

            it("false를 반환한다") {
                result shouldBe false
            }
        }
    }
})

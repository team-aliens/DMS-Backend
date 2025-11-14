package team.aliens.dms.domain.notification.service.impl

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.domain.notification.service.CheckNotificationServiceImpl
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import java.util.UUID

class CheckNotificationServiceImplTest : DescribeSpec({

    val queryDeviceTokenPort = mockk<QueryDeviceTokenPort>()

    val checkNotificationService = CheckNotificationServiceImpl(
        deviceTokenPort = queryDeviceTokenPort
    )

    describe("checkDeviceTokenByUserId") {
        context("사용자 ID로 디바이스 토큰 존재 여부를 확인하고 존재하면") {
            val userId = UUID.randomUUID()

            every { queryDeviceTokenPort.existsDeviceTokenByUserId(userId) } returns true

            it("true를 반환한다") {
                val result = checkNotificationService.checkDeviceTokenByUserId(userId)

                result shouldBe true
            }
        }

        context("사용자 ID로 디바이스 토큰 존재 여부를 확인하고 존재하지 않으면") {
            val userId = UUID.randomUUID()

            every { queryDeviceTokenPort.existsDeviceTokenByUserId(userId) } returns false

            it("false를 반환한다") {
                val result = checkNotificationService.checkDeviceTokenByUserId(userId)

                result shouldBe false
            }
        }
    }
})

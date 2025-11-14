package team.aliens.dms.domain.notification.dto

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.util.UUID

class SetDeviceTokenRequestTest : DescribeSpec({

    describe("toDeviceToken") {
        context("SetDeviceTokenRequest를 DeviceToken으로 변환하면") {
            val userId = UUID.randomUUID()
            val schoolId = UUID.randomUUID()
            val token = "test-device-token"
            val request = SetDeviceTokenRequest(token = token)

            it("DeviceToken을 반환한다") {
                val deviceToken = request.toDeviceToken(userId, schoolId)

                deviceToken.userId shouldBe userId
                deviceToken.schoolId shouldBe schoolId
                deviceToken.token shouldBe token
            }
        }
    }
})

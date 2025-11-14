package team.aliens.dms.domain.notification.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.contract.model.notification.DeviceTokenInfo
import team.aliens.dms.stub.createDeviceTokenStub
import java.util.UUID

class DeviceTokenTest : DescribeSpec({

    describe("DeviceToken.from") {
        context("DeviceTokenInfo로부터 DeviceToken을 생성하면") {
            val id = UUID.randomUUID()
            val userId = UUID.randomUUID()
            val schoolId = UUID.randomUUID()
            val token = "test-token"

            val deviceTokenInfo = DeviceTokenInfo(
                id = id,
                userId = userId,
                schoolId = schoolId,
                token = token
            )

            it("DeviceToken을 반환한다") {
                val deviceToken = DeviceToken.from(deviceTokenInfo)

                deviceToken.id shouldBe id
                deviceToken.userId shouldBe userId
                deviceToken.schoolId shouldBe schoolId
                deviceToken.token shouldBe token
            }
        }
    }

    describe("toDeviceTokenInfo") {
        context("DeviceToken을 DeviceTokenInfo로 변환하면") {
            val deviceToken = createDeviceTokenStub()

            it("DeviceTokenInfo를 반환한다") {
                val deviceTokenInfo = deviceToken.toDeviceTokenInfo()

                deviceTokenInfo.id shouldBe deviceToken.id
                deviceTokenInfo.userId shouldBe deviceToken.userId
                deviceTokenInfo.schoolId shouldBe deviceToken.schoolId
                deviceTokenInfo.token shouldBe deviceToken.token
            }
        }
    }
})

package team.aliens.dms.common.service.security

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.common.spi.SecurityPort
import java.util.UUID

class SecurityServiceImplTest : DescribeSpec({

    val securityPort = mockk<SecurityPort>()
    val securityService = SecurityServiceImpl(securityPort)

    describe("getCurrentUserId") {
        context("현재 사용자 ID를 조회하면") {
            val userId = UUID.randomUUID()

            every { securityPort.getCurrentUserId() } returns userId

            it("사용자 ID를 반환한다") {
                val result = securityService.getCurrentUserId()

                result shouldBe userId
            }
        }
    }

    describe("getCurrentSchoolId") {
        context("현재 학교 ID를 조회하면") {
            val schoolId = UUID.randomUUID()

            every { securityPort.getCurrentUserSchoolId() } returns schoolId

            it("학교 ID를 반환한다") {
                val result = securityService.getCurrentSchoolId()

                result shouldBe schoolId
            }
        }
    }
})

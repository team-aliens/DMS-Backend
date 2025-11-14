package team.aliens.dms.domain.notification.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.notification.dto.SetDeviceTokenRequest
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.stub.createDeviceTokenStub
import java.util.UUID

class SetDeviceTokenUseCaseTest : DescribeSpec({

    val securityService = mockk<SecurityService>()
    val notificationService = mockk<NotificationService>()

    val setDeviceTokenUseCase = SetDeviceTokenUseCase(
        securityService = securityService,
        notificationService = notificationService
    )

    describe("execute") {
        context("디바이스 토큰을 설정하면") {
            val userId = UUID.randomUUID()
            val schoolId = UUID.randomUUID()
            val token = "test-device-token"
            val request = SetDeviceTokenRequest(token = token)
            val deviceToken = createDeviceTokenStub(
                userId = userId,
                schoolId = schoolId,
                token = token
            )

            every { securityService.getCurrentUserId() } returns userId
            every { securityService.getCurrentSchoolId() } returns schoolId
            every { notificationService.saveDeviceToken(any()) } returns deviceToken

            it("디바이스 토큰을 저장한다") {
                setDeviceTokenUseCase.execute(request)

                verify(exactly = 1) { securityService.getCurrentUserId() }
                verify(exactly = 1) { securityService.getCurrentSchoolId() }
                verify(exactly = 1) { notificationService.saveDeviceToken(any()) }
            }
        }
    }
})

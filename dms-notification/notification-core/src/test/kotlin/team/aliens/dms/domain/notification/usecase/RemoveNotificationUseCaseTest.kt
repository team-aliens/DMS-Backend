package team.aliens.dms.domain.notification.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.notification.service.NotificationService
import java.util.UUID

class RemoveNotificationUseCaseTest : DescribeSpec({

    val securityService = mockk<SecurityService>()
    val notificationService = mockk<NotificationService>()

    val removeNotificationUseCase = RemoveNotificationUseCase(
        securityService = securityService,
        notificationService = notificationService
    )

    describe("execute") {
        context("알림을 삭제하면") {
            val userId = UUID.randomUUID()
            val notificationOfUserId = UUID.randomUUID()

            every { securityService.getCurrentUserId() } returns userId
            every { notificationService.deleteNotificationOfUserByUserIdAndId(userId, notificationOfUserId) } returns Unit

            it("알림을 삭제한다") {
                removeNotificationUseCase.execute(notificationOfUserId)

                verify(exactly = 1) { securityService.getCurrentUserId() }
                verify(exactly = 1) { notificationService.deleteNotificationOfUserByUserIdAndId(userId, notificationOfUserId) }
            }
        }
    }
})

package team.aliens.dms.domain.notification.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.domain.notification.service.NotificationService

class DeleteOldNotificationsUseCaseTest : DescribeSpec({

    val notificationService = mockk<NotificationService>()

    val deleteOldNotificationsUseCase = DeleteOldNotificationsUseCase(
        notificationService = notificationService
    )

    describe("execute") {
        context("오래된 알림을 삭제하면") {
            every { notificationService.deleteOldNotifications() } returns Unit

            it("60일 이상 지난 알림을 삭제한다") {
                deleteOldNotificationsUseCase.execute()

                verify(exactly = 1) { notificationService.deleteOldNotifications() }
            }
        }
    }
})

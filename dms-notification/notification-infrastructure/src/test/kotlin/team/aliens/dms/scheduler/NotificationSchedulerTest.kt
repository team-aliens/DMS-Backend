package team.aliens.dms.scheduler

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.domain.notification.usecase.DeleteOldNotificationsUseCase

class NotificationSchedulerTest : DescribeSpec({

    val deleteOldNotificationsUseCase = mockk<DeleteOldNotificationsUseCase>()
    val notificationScheduler = NotificationScheduler(deleteOldNotificationsUseCase)

    describe("deleteOldNotifications") {
        context("스케줄러가 실행되면") {
            every { deleteOldNotificationsUseCase.execute() } returns Unit

            it("오래된 알림 삭제 UseCase를 실행한다") {
                notificationScheduler.deleteOldNotifications()

                verify(exactly = 1) { deleteOldNotificationsUseCase.execute() }
            }
        }
    }
})

package team.aliens.dms.domain.notification.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.stub.createNotificationOfUserStub
import java.util.UUID

class QueryAndReadMyNotificationsUseCaseTest : DescribeSpec({

    val securityService = mockk<SecurityService>()
    val notificationService = mockk<NotificationService>()

    val queryAndReadMyNotificationsUseCase = QueryAndReadMyNotificationsUseCase(
        securityService = securityService,
        notificationService = notificationService
    )

    describe("execute") {
        context("내 알림 목록을 조회하고 읽음 처리하면") {
            val userId = UUID.randomUUID()
            val notifications = listOf(
                createNotificationOfUserStub(userId = userId, isRead = false),
                createNotificationOfUserStub(userId = userId, isRead = false)
            )

            every { securityService.getCurrentUserId() } returns userId
            every { notificationService.getNotificationOfUsersByUserId(userId) } returns notifications
            every { notificationService.saveNotificationsOfUser(any()) } returns Unit

            it("알림 목록을 조회하고 읽음 처리한다") {
                val result = queryAndReadMyNotificationsUseCase.execute()

                result shouldNotBe null
                verify(exactly = 1) { securityService.getCurrentUserId() }
                verify(exactly = 1) { notificationService.getNotificationOfUsersByUserId(userId) }
                verify(exactly = 1) { notificationService.saveNotificationsOfUser(any()) }
            }
        }
    }
})

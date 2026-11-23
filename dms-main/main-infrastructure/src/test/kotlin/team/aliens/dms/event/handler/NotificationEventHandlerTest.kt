package team.aliens.dms.event.handler

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.event.NotificationOutboxSavedEvent
import team.aliens.dms.event.NotificationOutboxSender
import java.util.UUID

class NotificationEventHandlerTest : DescribeSpec({

    val notificationOutboxSender = mockk<NotificationOutboxSender>(relaxed = true)
    val notificationEventHandler = NotificationEventHandler(notificationOutboxSender)

    describe("handleNotification") {
        context("알림 outbox 가 저장됐다는 이벤트가 발생하면") {
            val outboxId = UUID.randomUUID()

            it("해당 outbox 를 바로 발송한다") {
                notificationEventHandler.handleNotification(NotificationOutboxSavedEvent(outboxId))

                verify(exactly = 1) { notificationOutboxSender.send(outboxId) }
            }
        }
    }
})

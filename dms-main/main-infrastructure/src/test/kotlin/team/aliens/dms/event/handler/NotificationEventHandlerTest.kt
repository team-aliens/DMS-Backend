package team.aliens.dms.event.handler

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.contract.model.notification.NotificationInfo
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.event.GroupNotificationEvent
import team.aliens.dms.event.SingleNotificationEvent
import team.aliens.dms.event.TopicNotificationEvent
import java.util.UUID

class NotificationEventHandlerTest : DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val notificationService = mockk<NotificationService>(relaxed = true)
    val notificationEventHandler = NotificationEventHandler(notificationService)

    describe("handleNotification") {
        context("SingleNotificationEvent가 발생하면") {
            val userId = UUID.randomUUID()
            val notificationInfo = mockk<NotificationInfo>(relaxed = true)
            val deviceToken = mockk<DeviceToken>(relaxed = true)
            val event = SingleNotificationEvent(
                userId = userId,
                notificationInfo = notificationInfo
            )

            every { notificationService.getDeviceTokenByUserId(userId) } returns deviceToken

            it("해당 유저의 디바이스 토큰으로 알림을 보낸다") {
                notificationEventHandler.handleNotification(event)

                verify {
                    notificationService.getDeviceTokenByUserId(userId)
                    notificationService.sendMessage(deviceToken, any<Notification>())
                }
            }
        }

        context("GroupNotificationEvent가 발생하면") {
            val userIds = listOf(UUID.randomUUID(), UUID.randomUUID())
            val notificationInfo = mockk<NotificationInfo>(relaxed = true)
            val deviceTokens = listOf(mockk<DeviceToken>(relaxed = true))
            val event = GroupNotificationEvent(
                userIds = userIds,
                notificationInfo = notificationInfo
            )

            every { notificationService.getDiviceTokensByUserIds(userIds) } returns deviceTokens

            it("해당 유저들의 디바이스 토큰들로 알림을 보낸다") {
                notificationEventHandler.handleNotification(event)

                verify {
                    notificationService.getDiviceTokensByUserIds(userIds)
                    notificationService.sendMessages(deviceTokens, any<Notification>())
                }
            }
        }

        context("TopicNotificationEvent가 발생하면") {
            val notificationInfo = mockk<NotificationInfo>(relaxed = true)
            val event = TopicNotificationEvent(notificationInfo = notificationInfo)

            it("토픽 구독자들에게 알림을 보낸다") {
                notificationEventHandler.handleNotification(event)

                verify {
                    notificationService.sendMessagesByTopic(any<Notification>())
                }
            }
        }
    }
})

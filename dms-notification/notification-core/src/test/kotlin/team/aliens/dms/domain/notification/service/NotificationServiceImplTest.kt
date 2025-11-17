package team.aliens.dms.domain.notification.service

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.TopicSubscription
import team.aliens.dms.domain.notification.spi.CommandNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.CommandTopicSubscriptionPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import team.aliens.dms.domain.notification.spi.QueryTopicSubscriptionPort
import java.util.UUID

class NotificationServiceImplTest : DescribeSpec({

    val notificationPort = mockk<NotificationPort>()
    val queryDeviceTokenPort = mockk<QueryDeviceTokenPort>()
    val commandTopicSubscriptionPort = mockk<CommandTopicSubscriptionPort>()
    val notificationOfUserPort = mockk<CommandNotificationOfUserPort>()
    val queryTopicSubscriptionPort = mockk<QueryTopicSubscriptionPort>()
    val getNotificationService = mockk<GetNotificationService>()
    val checkNotificationService = mockk<CheckNotificationService>()
    val commandNotificationService = mockk<CommandNotificationService>()

    val service = NotificationServiceImpl(
        notificationPort = notificationPort,
        queryDeviceTokenPort = queryDeviceTokenPort,
        commandTopicSubscriptionPort = commandTopicSubscriptionPort,
        notificationOfUserPort = notificationOfUserPort,
        queryTopicSubscriptionPort = queryTopicSubscriptionPort,
        getNotificationService = getNotificationService,
        checkNotificationService = checkNotificationService,
        commandNotificationService = commandNotificationService
    )

    describe("subscribeTopic") {
        context("토큰과 주제로 구독하면") {
            val token = "device-token"
            val topic = Topic.NOTICE
            val deviceToken = DeviceToken(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                schoolId = UUID.randomUUID(),
                token = token
            )

            every { getNotificationService.getDeviceTokenByToken(token) } returns deviceToken
            every { commandTopicSubscriptionPort.saveTopicSubscription(any()) } returns mockk()
            every { notificationPort.subscribeTopic(token, topic) } just runs

            service.subscribeTopic(token, topic)

            it("주제 구독을 저장한다") {
                verify(exactly = 1) {
                    commandTopicSubscriptionPort.saveTopicSubscription(
                        match {
                            it.deviceTokenId == deviceToken.id &&
                            it.topic == topic &&
                            it.isSubscribed
                        }
                    )
                }
            }

            it("알림 포트로 구독을 요청한다") {
                verify(exactly = 1) { notificationPort.subscribeTopic(token, topic) }
            }
        }
    }

    describe("unsubscribeTopic") {
        context("토큰과 주제로 구독 해제하면") {
            val token = "device-token"
            val topic = Topic.NOTICE
            val deviceToken = DeviceToken(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                schoolId = UUID.randomUUID(),
                token = token
            )

            every { getNotificationService.getDeviceTokenByToken(token) } returns deviceToken
            every { commandTopicSubscriptionPort.saveTopicSubscription(any()) } returns mockk()
            every { notificationPort.unsubscribeTopic(token, topic) } just runs

            service.unsubscribeTopic(token, topic)

            it("주제 구독 해제를 저장한다") {
                verify(exactly = 1) {
                    commandTopicSubscriptionPort.saveTopicSubscription(
                        match {
                            it.deviceTokenId == deviceToken.id &&
                            it.topic == topic &&
                            !it.isSubscribed
                        }
                    )
                }
            }

            it("알림 포트로 구독 해제를 요청한다") {
                verify(exactly = 1) { notificationPort.unsubscribeTopic(token, topic) }
            }
        }
    }

    describe("sendMessage") {
        context("저장이 필요한 알림을 전송하면") {
            val deviceToken = DeviceToken(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                schoolId = UUID.randomUUID(),
                token = "token"
            )
            val notification = Notification(
                schoolId = UUID.randomUUID(),
                topic = Topic.NOTICE,
                linkIdentifier = null,
                title = "title",
                content = "content",
                threadId = "thread-id",
                isSaveRequired = true
            )

            every { notificationOfUserPort.saveNotificationOfUser(any()) } returns mockk()
            every { notificationPort.sendMessage(any(), any()) } just runs

            service.sendMessage(deviceToken, notification)

            it("알림을 저장한다") {
                verify(exactly = 1) { notificationOfUserPort.saveNotificationOfUser(any()) }
            }

            it("알림을 전송한다") {
                verify(exactly = 1) { notificationPort.sendMessage(deviceToken.token, notification) }
            }
        }

        context("저장이 필요하지 않은 알림을 전송하면") {
            val deviceToken = DeviceToken(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                schoolId = UUID.randomUUID(),
                token = "token"
            )
            val notification = Notification(
                schoolId = UUID.randomUUID(),
                topic = Topic.NOTICE,
                linkIdentifier = null,
                title = "title",
                content = "content",
                threadId = "thread-id",
                isSaveRequired = false
            )

            every { notificationPort.sendMessage(any(), any()) } just runs

            it("알림을 전송한다") {
                service.sendMessage(deviceToken, notification)
                verify(exactly = 1) { notificationPort.sendMessage(deviceToken.token, notification) }
            }
        }
    }

    describe("sendMessages") {
        context("여러 디바이스에 저장이 필요한 알림을 전송하면") {
            val deviceTokens = listOf(
                DeviceToken(
                    id = UUID.randomUUID(),
                    userId = UUID.randomUUID(),
                    schoolId = UUID.randomUUID(),
                    token = "token1"
                ),
                DeviceToken(
                    id = UUID.randomUUID(),
                    userId = UUID.randomUUID(),
                    schoolId = UUID.randomUUID(),
                    token = "token2"
                )
            )
            val notification = Notification(
                schoolId = UUID.randomUUID(),
                topic = Topic.NOTICE,
                linkIdentifier = null,
                title = "title",
                content = "content",
                threadId = "thread-id",
                isSaveRequired = true
            )

            every { notificationOfUserPort.saveNotificationsOfUser(any()) } just runs
            every { notificationPort.sendMessages(any(), any()) } just runs

            service.sendMessages(deviceTokens, notification)

            it("여러 알림을 저장한다") {
                verify(exactly = 1) { notificationOfUserPort.saveNotificationsOfUser(any()) }
            }

            it("여러 알림을 전송한다") {
                verify(exactly = 1) {
                    notificationPort.sendMessages(
                        tokens = listOf("token1", "token2"),
                        notification = notification
                    )
                }
            }
        }
    }

})

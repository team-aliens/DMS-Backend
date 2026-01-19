package team.aliens.dms.domain.notification.service

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
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

            it("주제를 구독한다") {
                shouldNotThrowAny {
                    service.subscribeTopic(token, topic)
                }
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

            it("주제 구독을 해제한다") {
                shouldNotThrowAny {
                    service.unsubscribeTopic(token, topic)
                }
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

            it("알림을 전송한다") {
                shouldNotThrowAny {
                    service.sendMessage(deviceToken, notification)
                }
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
                shouldNotThrowAny {
                    service.sendMessage(deviceToken, notification)
                }
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

            it("여러 알림을 전송한다") {
                shouldNotThrowAny {
                    service.sendMessages(deviceTokens, notification)
                }
            }
        }
    }

})

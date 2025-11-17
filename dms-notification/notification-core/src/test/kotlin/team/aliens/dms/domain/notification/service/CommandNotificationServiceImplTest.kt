package team.aliens.dms.domain.notification.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.exception.NotificationOfUserNotFoundException
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.domain.notification.spi.CommandNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.notification.spi.QueryNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.TopicSubscriptionPort
import java.time.LocalDateTime
import java.util.UUID

class CommandNotificationServiceImplTest : DescribeSpec({

    val deviceTokenPort = mockk<DeviceTokenPort>()
    val notificationPort = mockk<NotificationPort>()
    val queryNotificationOfUserPort = mockk<QueryNotificationOfUserPort>()
    val commandNotificationOfUserPort = mockk<CommandNotificationOfUserPort>()
    val topicSubscriptionPort = mockk<TopicSubscriptionPort>()

    val service = CommandNotificationServiceImpl(
        deviceTokenPort = deviceTokenPort,
        notificationPort = notificationPort,
        queryNotificationOfUserPort = queryNotificationOfUserPort,
        commandNotificationOfUserPort = commandNotificationOfUserPort,
        topicSubscriptionPort = topicSubscriptionPort
    )

    describe("saveDeviceToken") {
        context("디바이스 토큰을 저장하면") {
            val deviceToken = DeviceToken(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                schoolId = UUID.randomUUID(),
                token = "device-token"
            )

            every { deviceTokenPort.saveDeviceToken(deviceToken) } returns deviceToken
            every { notificationPort.subscribeAllTopics(deviceToken.token) } just runs

            val result = service.saveDeviceToken(deviceToken)

            it("디바이스 토큰을 저장하고 반환한다") {
                result shouldBe deviceToken
            }

            it("모든 주제를 구독한다") {
                verify(exactly = 1) { notificationPort.subscribeAllTopics(deviceToken.token) }
            }
        }
    }

    describe("deleteDeviceTokenByUserId") {
        context("사용자 ID로 디바이스 토큰을 삭제하면") {
            val userId = UUID.randomUUID()
            val deviceToken = DeviceToken(
                id = UUID.randomUUID(),
                userId = userId,
                schoolId = UUID.randomUUID(),
                token = "token"
            )

            every { deviceTokenPort.queryDeviceTokenByUserId(userId) } returns deviceToken
            every { topicSubscriptionPort.deleteAllByDeviceTokenId(deviceToken.id) } just runs
            every { deviceTokenPort.deleteDeviceTokenByUserId(userId) } just runs

            service.deleteDeviceTokenByUserId(userId)

            it("주제 구독을 모두 삭제한다") {
                verify(exactly = 1) { topicSubscriptionPort.deleteAllByDeviceTokenId(deviceToken.id) }
            }

            it("디바이스 토큰을 삭제한다") {
                verify(exactly = 1) { deviceTokenPort.deleteDeviceTokenByUserId(userId) }
            }
        }

        context("존재하지 않는 사용자 ID로 삭제하면") {
            val userId = UUID.randomUUID()

            every { deviceTokenPort.queryDeviceTokenByUserId(userId) } returns null

            it("DeviceTokenNotFoundException을 던진다") {
                shouldThrow<DeviceTokenNotFoundException> {
                    service.deleteDeviceTokenByUserId(userId)
                }
            }
        }
    }

    describe("deleteNotificationOfUserByUserIdAndId") {
        context("사용자의 알림을 삭제하면") {
            val userId = UUID.randomUUID()
            val notificationId = UUID.randomUUID()
            val notification = NotificationOfUser(
                id = notificationId,
                userId = userId,
                topic = Topic.NOTICE,
                linkIdentifier = null,
                title = "title",
                content = "content",
                createdAt = LocalDateTime.now()
            )

            every { queryNotificationOfUserPort.queryNotificationOfUserById(notificationId) } returns notification
            every { commandNotificationOfUserPort.deleteNotificationOfUserById(notificationId) } just runs

            service.deleteNotificationOfUserByUserIdAndId(userId, notificationId)

            it("알림을 삭제한다") {
                verify(exactly = 1) { commandNotificationOfUserPort.deleteNotificationOfUserById(notificationId) }
            }
        }

        context("다른 사용자의 알림을 삭제하려고 하면") {
            val userId = UUID.randomUUID()
            val otherUserId = UUID.randomUUID()
            val notificationId = UUID.randomUUID()
            val notification = NotificationOfUser(
                id = notificationId,
                userId = otherUserId,
                topic = Topic.NOTICE,
                linkIdentifier = null,
                title = "title",
                content = "content",
                createdAt = LocalDateTime.now()
            )

            every { queryNotificationOfUserPort.queryNotificationOfUserById(notificationId) } returns notification

            it("NotificationOfUserNotFoundException을 던진다") {
                shouldThrow<NotificationOfUserNotFoundException> {
                    service.deleteNotificationOfUserByUserIdAndId(userId, notificationId)
                }
            }
        }

        context("존재하지 않는 알림을 삭제하려고 하면") {
            val userId = UUID.randomUUID()
            val notificationId = UUID.randomUUID()

            every { queryNotificationOfUserPort.queryNotificationOfUserById(notificationId) } returns null

            it("NotificationOfUserNotFoundException을 던진다") {
                shouldThrow<NotificationOfUserNotFoundException> {
                    service.deleteNotificationOfUserByUserIdAndId(userId, notificationId)
                }
            }
        }
    }

    describe("deleteNotificationOfUserByUserId") {
        context("사용자의 모든 알림을 삭제하면") {
            val userId = UUID.randomUUID()

            every { commandNotificationOfUserPort.deleteNotificationOfUserByUserId(userId) } just runs

            service.deleteNotificationOfUserByUserId(userId)

            it("모든 알림을 삭제한다") {
                verify(exactly = 1) { commandNotificationOfUserPort.deleteNotificationOfUserByUserId(userId) }
            }
        }
    }

    describe("saveNotificationOfUser") {
        context("알림을 저장하면") {
            val notification = NotificationOfUser(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                topic = Topic.NOTICE,
                linkIdentifier = null,
                title = "title",
                content = "content",
                createdAt = LocalDateTime.now()
            )

            every { commandNotificationOfUserPort.saveNotificationOfUser(notification) } returns notification

            val result = service.saveNotificationOfUser(notification)

            it("저장된 알림을 반환한다") {
                result shouldBe notification
            }
        }
    }

    describe("saveNotificationsOfUser") {
        context("여러 알림을 저장하면") {
            val notifications = listOf(
                NotificationOfUser(
                    id = UUID.randomUUID(),
                    userId = UUID.randomUUID(),
                    topic = Topic.NOTICE,
                    linkIdentifier = null,
                    title = "title1",
                    content = "content1",
                    createdAt = LocalDateTime.now()
                ),
                NotificationOfUser(
                    id = UUID.randomUUID(),
                    userId = UUID.randomUUID(),
                    topic = Topic.POINT,
                    linkIdentifier = null,
                    title = "title2",
                    content = "content2",
                    createdAt = LocalDateTime.now()
                )
            )

            every { commandNotificationOfUserPort.saveNotificationsOfUser(notifications) } just runs

            service.saveNotificationsOfUser(notifications)

            it("모든 알림을 저장한다") {
                verify(exactly = 1) { commandNotificationOfUserPort.saveNotificationsOfUser(notifications) }
            }
        }
    }

    describe("deleteOldNotifications") {
        context("오래된 알림을 삭제하면") {
            every { commandNotificationOfUserPort.deleteOldNotificationOfUsers(any()) } just runs

            service.deleteOldNotifications()

            it("60일 이전의 알림을 삭제한다") {
                verify(exactly = 1) {
                    commandNotificationOfUserPort.deleteOldNotificationOfUsers(
                        match { cutoffDate ->
                            val expectedCutoff = LocalDateTime.now().minusDays(60)
                            cutoffDate.isBefore(expectedCutoff.plusMinutes(1)) &&
                                cutoffDate.isAfter(expectedCutoff.minusMinutes(1))
                        }
                    )
                }
            }
        }
    }
})

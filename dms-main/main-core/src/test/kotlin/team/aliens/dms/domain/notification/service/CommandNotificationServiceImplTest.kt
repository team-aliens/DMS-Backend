package team.aliens.dms.domain.notification.service

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import team.aliens.dms.domain.notification.exception.NotificationOfUserNotFoundException
import team.aliens.dms.domain.notification.spi.CommandNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.notification.spi.QueryNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.TopicSubscriptionPort
import team.aliens.dms.domain.notification.stub.createDeviceTokenStub
import team.aliens.dms.domain.notification.stub.createNotificationOfUserStub
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
            val deviceToken = createDeviceTokenStub()

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
            val deviceToken = createDeviceTokenStub(userId = userId)

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

            it("예외 없이 아무 동작도 하지 않는다") {
                shouldNotThrowAny {
                    service.deleteDeviceTokenByUserId(userId)
                }
                verify(exactly = 0) { deviceTokenPort.deleteDeviceTokenByUserId(userId) }
            }
        }
    }

    describe("deleteNotificationOfUserByUserIdAndId") {
        context("사용자의 알림을 삭제하면") {
            val userId = UUID.randomUUID()
            val notification = createNotificationOfUserStub(userId = userId)

            every { queryNotificationOfUserPort.queryNotificationOfUserById(notification.id) } returns notification
            every { commandNotificationOfUserPort.deleteNotificationOfUserById(notification.id) } just runs

            service.deleteNotificationOfUserByUserIdAndId(userId, notification.id)

            it("알림을 삭제한다") {
                verify(exactly = 1) { commandNotificationOfUserPort.deleteNotificationOfUserById(notification.id) }
            }
        }

        context("다른 사용자의 알림을 삭제하려고 하면") {
            val userId = UUID.randomUUID()
            val notification = createNotificationOfUserStub(userId = UUID.randomUUID())

            every { queryNotificationOfUserPort.queryNotificationOfUserById(notification.id) } returns notification

            it("NotificationOfUserNotFoundException을 던지고 삭제하지 않는다") {
                shouldThrow<NotificationOfUserNotFoundException> {
                    service.deleteNotificationOfUserByUserIdAndId(userId, notification.id)
                }
                verify(exactly = 0) { commandNotificationOfUserPort.deleteNotificationOfUserById(notification.id) }
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
            val notification = createNotificationOfUserStub()

            every { commandNotificationOfUserPort.saveNotificationOfUser(notification) } returns notification

            val result = service.saveNotificationOfUser(notification)

            it("저장된 알림을 반환한다") {
                result shouldBe notification
            }
        }
    }

    describe("saveNotificationsOfUser") {
        context("여러 알림을 저장하면") {
            val notifications = listOf(createNotificationOfUserStub(), createNotificationOfUserStub())

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

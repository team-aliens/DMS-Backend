package team.aliens.dms.domain.notification.service.impl

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.domain.notification.exception.NotificationOfUserNotFoundException
import team.aliens.dms.domain.notification.service.CommandNotificationServiceImpl
import team.aliens.dms.domain.notification.spi.CommandNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.notification.spi.QueryNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.TopicSubscriptionPort
import team.aliens.dms.stub.createDeviceTokenStub
import team.aliens.dms.stub.createNotificationOfUserStub
import java.time.LocalDateTime
import java.util.UUID

class CommandNotificationServiceImplTest : DescribeSpec({

    val deviceTokenPort = mockk<DeviceTokenPort>()
    val notificationPort = mockk<NotificationPort>()
    val queryNotificationOfUserPort = mockk<QueryNotificationOfUserPort>()
    val commandNotificationOfUserPort = mockk<CommandNotificationOfUserPort>()
    val topicSubscriptionPort = mockk<TopicSubscriptionPort>()

    val commandNotificationService = CommandNotificationServiceImpl(
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
            every { notificationPort.subscribeAllTopics(deviceToken.token) } returns Unit

            it("디바이스 토큰을 저장하고 반환한다") {
                val result = commandNotificationService.saveDeviceToken(deviceToken)

                result shouldBe deviceToken
            }
        }
    }

    describe("deleteDeviceTokenByUserId") {
        context("사용자 ID로 디바이스 토큰을 삭제하고 토큰이 존재하면") {
            val userId = UUID.randomUUID()
            val deviceToken = createDeviceTokenStub(userId = userId)

            every { deviceTokenPort.queryDeviceTokenByUserId(userId) } returns deviceToken
            every { topicSubscriptionPort.deleteAllByDeviceTokenId(deviceToken.id) } returns Unit
            every { deviceTokenPort.deleteDeviceTokenByUserId(userId) } returns Unit

            it("주제 구독과 디바이스 토큰을 삭제한다") {
                shouldNotThrowAny {
                    commandNotificationService.deleteDeviceTokenByUserId(userId)
                }
            }
        }

        context("사용자 ID로 디바이스 토큰을 삭제하려는데 토큰이 존재하지 않으면") {
            val userId = UUID.randomUUID()

            every { deviceTokenPort.queryDeviceTokenByUserId(userId) } returns null

            it("예외 없이 아무 동작도 하지 않는다") {
                shouldNotThrowAny {
                    commandNotificationService.deleteDeviceTokenByUserId(userId)
                }
                verify(exactly = 0) { deviceTokenPort.deleteDeviceTokenByUserId(userId) }
            }
        }
    }

    describe("deleteNotificationOfUserByUserIdAndId") {
        context("사용자 ID와 알림 ID로 알림을 삭제하고 알림이 존재하면") {
            val userId = UUID.randomUUID()
            val notificationOfUserId = UUID.randomUUID()
            val notificationOfUser = createNotificationOfUserStub(
                id = notificationOfUserId,
                userId = userId
            )

            every { queryNotificationOfUserPort.queryNotificationOfUserById(notificationOfUserId) } returns notificationOfUser
            every { commandNotificationOfUserPort.deleteNotificationOfUserById(notificationOfUserId) } returns Unit

            it("알림을 삭제한다") {
                shouldNotThrowAny {
                    commandNotificationService.deleteNotificationOfUserByUserIdAndId(userId, notificationOfUserId)
                }
            }
        }

        context("알림이 존재하지 않으면") {
            val userId = UUID.randomUUID()
            val notificationOfUserId = UUID.randomUUID()

            every { queryNotificationOfUserPort.queryNotificationOfUserById(notificationOfUserId) } returns null

            it("NotificationOfUserNotFoundException을 발생시킨다") {
                shouldThrow<NotificationOfUserNotFoundException> {
                    commandNotificationService.deleteNotificationOfUserByUserIdAndId(userId, notificationOfUserId)
                }
            }
        }

        context("알림의 사용자 ID가 일치하지 않으면") {
            val userId = UUID.randomUUID()
            val notificationOfUserId = UUID.randomUUID()
            val differentUserId = UUID.randomUUID()
            val notificationOfUser = createNotificationOfUserStub(
                id = notificationOfUserId,
                userId = differentUserId
            )

            every { queryNotificationOfUserPort.queryNotificationOfUserById(notificationOfUserId) } returns notificationOfUser

            it("NotificationOfUserNotFoundException을 발생시킨다") {
                shouldThrow<NotificationOfUserNotFoundException> {
                    commandNotificationService.deleteNotificationOfUserByUserIdAndId(userId, notificationOfUserId)
                }
            }
        }
    }

    describe("deleteNotificationOfUserByUserId") {
        context("사용자 ID로 모든 알림을 삭제하면") {
            val userId = UUID.randomUUID()

            every { commandNotificationOfUserPort.deleteNotificationOfUserByUserId(userId) } returns Unit

            it("사용자의 모든 알림을 삭제한다") {
                shouldNotThrowAny {
                    commandNotificationService.deleteNotificationOfUserByUserId(userId)
                }
            }
        }
    }

    describe("saveNotificationOfUser") {
        context("알림을 저장하면") {
            val notificationOfUser = createNotificationOfUserStub()

            every { commandNotificationOfUserPort.saveNotificationOfUser(notificationOfUser) } returns notificationOfUser

            it("알림을 저장하고 반환한다") {
                val result = commandNotificationService.saveNotificationOfUser(notificationOfUser)

                result shouldBe notificationOfUser
            }
        }
    }

    describe("saveNotificationsOfUser") {
        context("알림 목록을 저장하면") {
            val notificationOfUsers = listOf(
                createNotificationOfUserStub(),
                createNotificationOfUserStub()
            )

            every { commandNotificationOfUserPort.saveNotificationsOfUser(notificationOfUsers) } returns Unit

            it("알림 목록을 저장한다") {
                shouldNotThrowAny {
                    commandNotificationService.saveNotificationsOfUser(notificationOfUsers)
                }
            }
        }
    }

    describe("deleteOldNotifications") {
        context("60일 이상 지난 알림을 삭제하면") {
            every { commandNotificationOfUserPort.deleteOldNotificationOfUsers(any<LocalDateTime>()) } returns Unit

            it("오래된 알림을 삭제한다") {
                shouldNotThrowAny {
                    commandNotificationService.deleteOldNotifications()
                }
            }
        }
    }
})

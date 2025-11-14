package team.aliens.dms.persistence.notification

import com.querydsl.jpa.impl.JPAQueryFactory
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.persistence.notification.entity.NotificationOfUserJpaEntity
import team.aliens.dms.persistence.notification.mapper.NotificationOfUserMapper
import team.aliens.dms.persistence.notification.repository.NotificationOfUserJpaRepository
import java.time.LocalDateTime
import java.util.UUID

class NotificationOfPersistenceAdapterOfUserTest : DescribeSpec({

    val mapper = mockk<NotificationOfUserMapper>()
    val repository = mockk<NotificationOfUserJpaRepository>()
    val queryFactory = mockk<JPAQueryFactory>()

    val adapter = NotificationOfPersistenceAdapterOfUser(
        notificationOfUserMapper = mapper,
        notificationOfUserRepository = repository,
        queryFactory = queryFactory
    )

    describe("saveNotificationOfUser") {
        context("알림을 저장하면") {
            val notification = NotificationOfUser(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                topic = Topic.NOTICE,
                linkIdentifier = null,
                title = "Test",
                content = "Test Content",
                createdAt = LocalDateTime.now(),
                isRead = false
            )
            val entity = mockk<NotificationOfUserJpaEntity>()

            every { mapper.toEntity(notification) } returns entity
            every { repository.save(entity) } returns entity
            every { mapper.toDomain(entity) } returns notification

            it("저장된 NotificationOfUser를 반환한다") {
                val result = adapter.saveNotificationOfUser(notification)

                result shouldBe notification
                verify(exactly = 1) { mapper.toEntity(notification) }
                verify(exactly = 1) { repository.save(entity) }
            }
        }
    }

    describe("saveNotificationsOfUser") {
        context("알림 목록을 저장하면") {
            val notifications = listOf(
                NotificationOfUser(
                    id = UUID.randomUUID(),
                    userId = UUID.randomUUID(),
                    topic = Topic.NOTICE,
                    linkIdentifier = null,
                    title = "Test",
                    content = "Test Content",
                    createdAt = LocalDateTime.now(),
                    isRead = false
                )
            )
            val entities = mockk<List<NotificationOfUserJpaEntity>>()

            every { mapper.toEntity(any()) } returns mockk()
            every { repository.saveAll(any<List<NotificationOfUserJpaEntity>>()) } returns entities

            it("알림 목록을 저장한다") {
                adapter.saveNotificationsOfUser(notifications)

                verify(exactly = 1) { repository.saveAll(any<List<NotificationOfUserJpaEntity>>()) }
            }
        }
    }

    describe("deleteNotificationOfUserById") {
        context("ID로 알림을 삭제하면") {
            val notificationId = UUID.randomUUID()

            every { repository.deleteById(notificationId) } returns Unit

            it("알림을 삭제한다") {
                adapter.deleteNotificationOfUserById(notificationId)

                verify(exactly = 1) { repository.deleteById(notificationId) }
            }
        }
    }

    describe("deleteNotificationOfUserByUserId") {
        context("사용자 ID로 모든 알림을 삭제하면") {
            val userId = UUID.randomUUID()

            every { repository.deleteByUserId(userId) } returns Unit

            it("사용자의 모든 알림을 삭제한다") {
                adapter.deleteNotificationOfUserByUserId(userId)

                verify(exactly = 1) { repository.deleteByUserId(userId) }
            }
        }
    }
})

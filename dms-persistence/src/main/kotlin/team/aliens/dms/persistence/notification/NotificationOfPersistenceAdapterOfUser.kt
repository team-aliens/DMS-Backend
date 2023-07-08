package team.aliens.dms.persistence.notification

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.domain.notification.spi.NotificationOfUserPort
import team.aliens.dms.persistence.notification.mapper.NotificationOfUserMapper
import team.aliens.dms.persistence.notification.repository.NotificationOfUserJpaRepository
import java.util.UUID

@Component
class NotificationOfPersistenceAdapterOfUser(
    private val notificationOfUserMapper: NotificationOfUserMapper,
    private val notificationOfUserRepository: NotificationOfUserJpaRepository
) : NotificationOfUserPort {

    override fun saveNotificationOfUser(notificationOfUser: NotificationOfUser) =
        notificationOfUserMapper.toDomain(
            notificationOfUserRepository.save(
                notificationOfUserMapper.toEntity(notificationOfUser)
            )
        )!!

    override fun saveNotificationsOfUser(notificationsOfUser: List<NotificationOfUser>) {
        notificationOfUserRepository.saveAll(
            notificationsOfUser.map {
                notificationOfUserMapper.toEntity(it)
            }
        )
    }

    override fun queryNotificationOfUserByUserId(userId: UUID) =
        notificationOfUserRepository.findByUserId(userId)
            .map { notificationOfUserMapper.toDomain(it)!! }

    override fun queryNotificationOfUserById(notificationOfUserId: UUID) = notificationOfUserMapper.toDomain(
        notificationOfUserRepository.findByIdOrNull(notificationOfUserId)
    )

    override fun deleteNotificationOfUserById(notificationOfUserId: UUID) {
        notificationOfUserRepository.deleteById(notificationOfUserId)
    }

    override fun deleteNotificationOfUserByUserId(userId: UUID) {
        notificationOfUserRepository.deleteByUserId(userId)
    }
}

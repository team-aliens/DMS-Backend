package team.aliens.dms.persistence.notification

import java.util.UUID
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.UserNotification
import team.aliens.dms.domain.notification.spi.UserNotificationPort
import team.aliens.dms.persistence.notification.mapper.UserNotificationMapper
import team.aliens.dms.persistence.notification.repository.UserNotificationJpaRepository

@Component
class UserNotificationPersistenceAdapter(
    private val userNotificationMapper: UserNotificationMapper,
    private val userNotificationRepository: UserNotificationJpaRepository
) : UserNotificationPort {

    override fun saveUserNotification(userNotification: UserNotification) =
        userNotificationMapper.toDomain(
            userNotificationRepository.save(
                userNotificationMapper.toEntity(userNotification)
            )
        )!!

    override fun saveUserNotifications(userNotifications: List<UserNotification>) {
        userNotificationRepository.saveAll(
            userNotifications.map {
                userNotificationMapper.toEntity(it)
            }
        )
    }

    override fun queryUserNotificationByUserId(userId: UUID) =
        userNotificationRepository.findByUserId(userId)
            .map { userNotificationMapper.toDomain(it)!! }
}

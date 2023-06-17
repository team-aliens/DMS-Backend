package team.aliens.dms.persistence.notification.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.UserNotification
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.notification.entity.UserNotificationJpaEntity
import team.aliens.dms.persistence.user.repository.UserJpaRepository

@Component
class UserNotificationMapper(
    private val userRepository: UserJpaRepository
) : GenericMapper<UserNotification, UserNotificationJpaEntity> {

    override fun toDomain(entity: UserNotificationJpaEntity?): UserNotification? {
        return entity?.let {
            UserNotification(
                id = it.id!!,
                userId = it.user!!.id!!,
                topic = it.topic,
                identifier = it.identifier,
                title = it.title,
                content = it.content,
                createdAt = it.createdAt
            )
        }
    }

    override fun toEntity(domain: UserNotification): UserNotificationJpaEntity {
        val user = userRepository.findByIdOrNull(domain.userId) ?: throw UserNotFoundException

        return UserNotificationJpaEntity(
            id = domain.id,
            user = user,
            topic = domain.topic,
            identifier = domain.identifier,
            title = domain.title,
            content = domain.content,
            createdAt = domain.createdAt
        )
    }
}

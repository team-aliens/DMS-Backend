package team.aliens.dms.persistence.notification.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.notification.entity.NotificationOfUserJpaEntity
import team.aliens.dms.persistence.user.repository.UserJpaRepository

@Component
class NotificationOfUserMapper(
    private val userRepository: UserJpaRepository
) : GenericMapper<NotificationOfUser, NotificationOfUserJpaEntity> {

    override fun toDomain(entity: NotificationOfUserJpaEntity?): NotificationOfUser? {
        return entity?.let {
            NotificationOfUser(
                id = it.id!!,
                userId = it.user!!.id!!,
                topic = it.topic,
                linkIdentifier = it.linkIdentifier,
                title = it.title,
                content = it.content,
                createdAt = it.createdAt
            )
        }
    }

    override fun toEntity(domain: NotificationOfUser): NotificationOfUserJpaEntity {
        val user = userRepository.findByIdOrNull(domain.userId) ?: throw UserNotFoundException

        return NotificationOfUserJpaEntity(
            id = domain.id,
            user = user,
            topic = domain.topic,
            linkIdentifier = domain.linkIdentifier,
            title = domain.title,
            content = domain.content,
            createdAt = domain.createdAt
        )
    }
}

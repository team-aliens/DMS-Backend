package team.aliens.dms.persistence.notification.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.contract.model.notification.PointDetailTopic
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.notification.entity.NotificationOfUserJpaEntity

@Component
class NotificationOfUserMapper : GenericMapper<NotificationOfUser, NotificationOfUserJpaEntity> {

    override fun toDomain(entity: NotificationOfUserJpaEntity?): NotificationOfUser? {
        return entity?.let {
            NotificationOfUser(
                id = it.id!!,
                userId = it.userId!!,
                topic = it.topic,
                pointDetailTopic = it.pointDetailTopic,
                linkIdentifier = it.linkIdentifier,
                title = it.title,
                content = it.content,
                createdAt = it.createdAt,
                isRead = it.isRead
            )
        }
    }

    override fun toEntity(domain: NotificationOfUser): NotificationOfUserJpaEntity =
        NotificationOfUserJpaEntity(
            id = domain.id,
            userId = domain.userId,
            topic = domain.topic,
            pointDetailTopic = domain.pointDetailTopic,
            linkIdentifier = domain.linkIdentifier,
            title = domain.title,
            content = domain.content,
            createdAt = domain.createdAt,
            isRead = domain.isRead
        )
}

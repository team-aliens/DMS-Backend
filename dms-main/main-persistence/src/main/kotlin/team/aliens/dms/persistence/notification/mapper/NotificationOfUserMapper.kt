package team.aliens.dms.persistence.notification.mapper

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.notification.entity.NotificationOfUserJpaEntity
import team.aliens.dms.persistence.user.entity.UserJpaEntity

// 알림은 학교 단위로 한꺼번에 저장되므로 유저를 조회하지 않고 참조(getReference)만 건다
@Component
class NotificationOfUserMapper(
    private val entityManager: EntityManager
) : GenericMapper<NotificationOfUser, NotificationOfUserJpaEntity> {

    override fun toDomain(entity: NotificationOfUserJpaEntity?): NotificationOfUser? {
        return entity?.let {
            NotificationOfUser(
                id = it.id!!,
                userId = it.user!!.id!!,
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
            user = entityManager.getReference(UserJpaEntity::class.java, domain.userId),
            topic = domain.topic,
            pointDetailTopic = domain.pointDetailTopic,
            linkIdentifier = domain.linkIdentifier,
            title = domain.title,
            content = domain.content,
            createdAt = domain.createdAt,
            isRead = domain.isRead
        )
}

package team.aliens.dms.persistence.notification.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.contract.model.notification.NotificationInfo
import team.aliens.dms.domain.notification.model.NotificationOutbox
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.notification.entity.NotificationOutboxJpaEntity

@Component
class NotificationOutboxMapper : GenericMapper<NotificationOutbox, NotificationOutboxJpaEntity> {

    override fun toDomain(entity: NotificationOutboxJpaEntity?): NotificationOutbox? {
        return entity?.let {
            NotificationOutbox(
                id = it.id!!,
                userIds = it.userIds,
                notificationInfo = NotificationInfo(
                    schoolId = it.schoolId,
                    topic = it.topic,
                    pointDetailTopic = it.pointDetailTopic,
                    linkIdentifier = it.linkIdentifier,
                    title = it.title,
                    content = it.content,
                    threadId = it.threadId,
                    createdAt = it.createdAt,
                    isSaveRequired = it.isSaveRequired
                ),
                status = it.status,
                retryCount = it.retryCount
            )
        }
    }

    override fun toEntity(domain: NotificationOutbox): NotificationOutboxJpaEntity {
        val info = domain.notificationInfo
        return NotificationOutboxJpaEntity(
            id = domain.id,
            userIds = domain.userIds,
            schoolId = info.schoolId,
            topic = info.topic,
            pointDetailTopic = info.pointDetailTopic,
            linkIdentifier = info.linkIdentifier,
            title = info.title,
            content = info.content,
            threadId = info.threadId,
            isSaveRequired = info.isSaveRequired,
            status = domain.status,
            retryCount = domain.retryCount,
            createdAt = info.createdAt
        )
    }
}

package team.aliens.dms.persistence.notice.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.manager.repository.ManagerJpaRepository
import team.aliens.dms.persistence.notice.entity.NoticeJpaEntity

@Component
class NoticeMapper(
    private val managerRepository: ManagerJpaRepository
) : GenericMapper<Notice, NoticeJpaEntity> {

    override fun toDomain(entity: NoticeJpaEntity?): Notice? {
        return entity?.let {
            Notice(
                id = it.id!!,
                managerId = it.manager!!.id,
                title = it.title,
                content = it.content,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }

    override fun toEntity(domain: Notice): NoticeJpaEntity {
        val manager = managerRepository.findByIdOrNull(domain.managerId)

        return NoticeJpaEntity(
            id = domain.id,
            manager = manager,
            title = domain.title,
            content = domain.content,
            createdAt = domain.createdAt!!,
            updatedAt = domain.updatedAt!!
        )
    }
}

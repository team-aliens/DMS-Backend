package team.aliens.dms.persistence.tag.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.tag.model.Tag
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.tag.entity.TagJpaEntity

@Component
class TagMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<Tag, TagJpaEntity> {

    override fun toDomain(entity: TagJpaEntity?): Tag? {
        return entity?.let {
            Tag(
                id = it.id!!,
                name = it.name,
                color = it.color,
                schoolId = it.school!!.id!!
            )
        }
    }

    override fun toEntity(domain: Tag): TagJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return TagJpaEntity(
            id = domain.id,
            name = domain.name,
            color = domain.color,
            school = school
        )
    }
}

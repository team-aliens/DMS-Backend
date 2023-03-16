package team.aliens.dms.persistence.student.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.model.Tag
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.student.entity.TagJpaEntity

@Component
class TagMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<Tag, TagJpaEntity> {

    override fun toDomain(entity: TagJpaEntity?): Tag? {
        return entity?.let {
            Tag(
                id = it.id!!,
                name = it.name,
                schoolId = it.school!!.id!!
            )
        }
    }

    override fun toEntity(domain: Tag): TagJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.id)

        return TagJpaEntity(
            id = domain.id,
            name = domain.name,
            school = school
        )
    }
}

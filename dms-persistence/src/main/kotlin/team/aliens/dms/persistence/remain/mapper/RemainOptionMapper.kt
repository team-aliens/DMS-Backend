package team.aliens.dms.persistence.remain.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.remain.entity.RemainOptionJpaEntity
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository

@Component
class RemainOptionMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<RemainOption, RemainOptionJpaEntity> {

    override fun toDomain(entity: RemainOptionJpaEntity?): RemainOption? {
        return entity?.let {
            RemainOption(
                id = it.id!!,
                schoolId = it.school!!.id!!,
                title = it.title,
                description = it.description
            )
        }
    }

    override fun toEntity(domain: RemainOption): RemainOptionJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return RemainOptionJpaEntity(
            id = domain.id,
            school = school,
            title = domain.title,
            description = domain.description
        )
    }
}

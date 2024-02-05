package team.aliens.dms.persistence.outing.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.outing.entity.OutingTypeJpaEntity
import team.aliens.dms.persistence.outing.entity.OutingTypeJpaEntityId
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository

@Component
class OutingTypeMapper (
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<OutingType, OutingTypeJpaEntity> {

    override fun toDomain(entity: OutingTypeJpaEntity?): OutingType? {
        return entity?.let {
            OutingType(
                title = it.id.title,
                schoolId = it.id.schoolId
            )
        }
    }

    override fun toEntity(domain: OutingType): OutingTypeJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return OutingTypeJpaEntity(
            id = OutingTypeJpaEntityId(
                title = domain.title,
                schoolId = domain.schoolId
            ),
            school = school
        )
    }
}

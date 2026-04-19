package team.aliens.dms.persistence.daybreak.mapper

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import team.aliens.dms.domain.daybreak.model.DaybreakStudyType
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.daybreak.entity.DaybreakStudyTypeJpaEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity

@Component
class DaybreakStudyTypeMapper(
    private val entityManager: EntityManager
) : GenericMapper<DaybreakStudyType, DaybreakStudyTypeJpaEntity> {

    override fun toDomain(entity: DaybreakStudyTypeJpaEntity?): DaybreakStudyType? {

        return entity?.let {
            DaybreakStudyType(
                id = it.id!!,
                name = it.name,
                schoolId = it.schoolJpaEntity!!.id!!
            )
        }
    }

    override fun toEntity(domain: DaybreakStudyType): DaybreakStudyTypeJpaEntity {

        return DaybreakStudyTypeJpaEntity(
            id = domain.id,
            name = domain.name,
            schoolJpaEntity = entityManager.getReference(SchoolJpaEntity::class.java, domain.schoolId)
        )
    }
}

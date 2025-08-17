package team.aliens.dms.persistence.point.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.point.entity.PointOptionJpaEntity
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository

@Component
class PointOptionMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<PointOption, PointOptionJpaEntity> {

    override fun toDomain(entity: PointOptionJpaEntity?): PointOption? {
        return entity?.let {
            PointOption(
                id = it.id!!,
                schoolId = it.school!!.id!!,
                name = it.name,
                score = it.score,
                type = it.type,
                createdAt = it.createdAt
            )
        }
    }

    override fun toEntity(domain: PointOption): PointOptionJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return PointOptionJpaEntity(
            id = domain.id,
            school = school,
            name = domain.name,
            score = domain.score,
            type = domain.type,
            createdAt = domain.createdAt!!
        )
    }
}

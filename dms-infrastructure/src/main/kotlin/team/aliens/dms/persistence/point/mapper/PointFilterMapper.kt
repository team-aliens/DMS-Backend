package team.aliens.dms.persistence.point.mapper

import org.springframework.data.repository.findByIdOrNull
import team.aliens.dms.domain.point.model.PointFilter
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.point.entity.PointFilterJpaEntity
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository

class PointFilterMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<PointFilter, PointFilterJpaEntity> {

    override fun toDomain(entity: PointFilterJpaEntity?): PointFilter? {
        return entity?.let {
            PointFilter(
                id = it.id!!,
                schoolId = it.school!!.id!!,
                name = it.name,
                pointType = it.pointType,
                maxPoint = it.maxPoint,
                minPoint = it.minPoint
            )
        }
    }

    override fun toEntity(domain: PointFilter): PointFilterJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return PointFilterJpaEntity(
            id = domain.id,
            school = school,
            name = domain.name,
            pointType = domain.pointType,
            maxPoint = domain.maxPoint,
            minPoint = domain.minPoint
        )
    }
}
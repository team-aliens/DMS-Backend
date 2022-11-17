package team.aliens.dms.persistence.feature.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.feature.AvailableService
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.feature.entity.AvailableServiceJpaEntity

@Component
class AvailableServiceMapper(
    private val schoolJpaRepository: SchoolJpaRepository
) : GenericMapper<AvailableService, AvailableServiceJpaEntity> {

    override fun toDomain(entity: AvailableServiceJpaEntity?): AvailableService? {
        return entity?.let {
            AvailableService(
                schoolId = entity.schoolId,
                mealService = entity.mealService,
                noticeService = entity.noticeService,
                pointService = entity.pointService
            )
        }
    }

    override fun toEntity(domain: AvailableService): AvailableServiceJpaEntity {
        val school = schoolJpaRepository.findByIdOrNull(domain.schoolId)

        return AvailableServiceJpaEntity(
            schoolId = domain.schoolId,
            school = school,
            mealService = domain.mealService,
            noticeService = domain.noticeService,
            pointService = domain.pointService
        )
    }
}
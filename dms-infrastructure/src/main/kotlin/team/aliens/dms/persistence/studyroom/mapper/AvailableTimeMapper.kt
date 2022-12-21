package team.aliens.dms.persistence.studyroom.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.AvailableTime
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.studyroom.entity.AvailableTimeJpaEntity

@Component
class AvailableTimeMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<AvailableTime, AvailableTimeJpaEntity> {

    override fun toDomain(entity: AvailableTimeJpaEntity?): AvailableTime? {
        return entity?.let {
            AvailableTime(
                schoolId = entity.schoolId,
                startAt = entity.startAt,
                endAt = entity.endAt
            )
        }
    }

    override fun toEntity(domain: AvailableTime): AvailableTimeJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return AvailableTimeJpaEntity(
            schoolId = domain.schoolId,
            school = school,
            startAt = domain.startAt,
            endAt = domain.endAt
        )
    }
}
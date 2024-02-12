package team.aliens.dms.persistence.outing.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.OutingAvailableTime
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.outing.entity.OutingAvailableTimeJpaEntity
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository

@Component
class OutingAvailableTimeMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<OutingAvailableTime, OutingAvailableTimeJpaEntity> {

    override fun toDomain(entity: OutingAvailableTimeJpaEntity?): OutingAvailableTime? {
        return entity?.let {
            OutingAvailableTime(
                id = it.id!!,
                schoolId = it.school!!.id!!,
                outingTime = it.outingTime,
                arrivalTime = it.arrivalTime,
                enabled = it.enabled,
                dayOfWeek = it.dayOfWeek
            )
        }
    }

    override fun toEntity(domain: OutingAvailableTime): OutingAvailableTimeJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return OutingAvailableTimeJpaEntity(
            id = domain.id,
            school = school,
            outingTime = domain.outingTime,
            arrivalTime = domain.arrivalTime,
            enabled = domain.enabled,
            dayOfWeek = domain.dayOfWeek
        )
    }
}

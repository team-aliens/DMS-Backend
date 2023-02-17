package team.aliens.dms.persistence.remain.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.remain.model.RemainAvailableTime
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.remain.entity.RemainAvailableTimeJpaEntity
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository

@Component
class RemainAvailableTimeMapper(
    private val schoolJpaRepository: SchoolJpaRepository
) : GenericMapper<RemainAvailableTime, RemainAvailableTimeJpaEntity> {

    override fun toDomain(entity: RemainAvailableTimeJpaEntity?): RemainAvailableTime? {
        return entity?.let {
            RemainAvailableTime(
                id = it.id,
                startTime = it.startTime,
                startDayOfWeek = it.startDayOfWeek,
                endTime = it.endTime,
                endDayOfWeek = it.endDayOfWeek
            )
        }
    }

    override fun toEntity(domain: RemainAvailableTime): RemainAvailableTimeJpaEntity {
        val school = schoolJpaRepository.findByIdOrNull(domain.id)

        return RemainAvailableTimeJpaEntity(
            id = domain.id,
            school = school,
            startTime = domain.startTime,
            startDayOfWeek = domain.startDayOfWeek,
            endTime = domain.endTime,
            endDayOfWeek = domain.endDayOfWeek
        )
    }
}
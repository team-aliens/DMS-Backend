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
                schoolId = it.id,
                startTime = it.startTime,
                startDayOfWalk = it.startDayOfWalk,
                endTime = it.endTime,
                endDayOfWalk = it.endDayOfWalk
            )
        }
    }

    override fun toEntity(domain: RemainAvailableTime): RemainAvailableTimeJpaEntity {
        val school = schoolJpaRepository.findByIdOrNull(domain.schoolId)

        return RemainAvailableTimeJpaEntity(
            id = domain.schoolId,
            school = school,
            startTime = domain.startTime,
            startDayOfWalk = domain.startDayOfWalk,
            endTime = domain.endTime,
            endDayOfWalk = domain.endDayOfWalk
        )
    }
}
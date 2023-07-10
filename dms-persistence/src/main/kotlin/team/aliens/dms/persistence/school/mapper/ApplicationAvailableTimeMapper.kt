package team.aliens.dms.persistence.school.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.school.model.ApplicationAvailableTime
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.entity.ApplicationAvailableTimeId
import team.aliens.dms.persistence.school.entity.ApplicationAvailableTimeJpaEntity
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository

@Component
class ApplicationAvailableTimeMapper(
    private val schoolJpaRepository: SchoolJpaRepository
) : GenericMapper<ApplicationAvailableTime, ApplicationAvailableTimeJpaEntity> {

    override fun toDomain(entity: ApplicationAvailableTimeJpaEntity?): ApplicationAvailableTime? {
        return entity?.let {
            ApplicationAvailableTime(
                type = it.id.type,
                schoolId = it.id.schoolId,
                startDayOfWeek = it.startDayOfWeek,
                startTime = it.startTime,
                endDayOfWeek = it.endDayOfWeek,
                endTime = it.endTime
            )
        }
    }

    override fun toEntity(domain: ApplicationAvailableTime): ApplicationAvailableTimeJpaEntity {
        val school = schoolJpaRepository.findByIdOrNull(domain.schoolId)

        return ApplicationAvailableTimeJpaEntity(
            id = ApplicationAvailableTimeId(
                type = domain.type,
                schoolId = domain.schoolId
            ),
            school = school,
            startDayOfWeek = domain.startDayOfWeek,
            startTime = domain.startTime,
            endDayOfWeek = domain.endDayOfWeek,
            endTime = domain.endTime
        )
    }
}

package team.aliens.dms.persistence.studyroom.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.studyroom.entity.TimeSlotJpaEntity

@Component
class TimeSlotMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<TimeSlot, TimeSlotJpaEntity> {

    override fun toDomain(entity: TimeSlotJpaEntity?): TimeSlot? {
        return entity?.let {
            TimeSlot(
                id = it.id!!,
                schoolId = it.school!!.id!!,
                startTime = it.startTime,
                endTime = it.endTime
            )
        }
    }

    override fun toEntity(domain: TimeSlot): TimeSlotJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)
        return TimeSlotJpaEntity(
            id = domain.id,
            school = school,
            startTime = domain.startTime,
            endTime = domain.endTime
        )
    }
}

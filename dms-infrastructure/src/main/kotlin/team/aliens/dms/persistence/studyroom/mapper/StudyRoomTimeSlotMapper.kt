package team.aliens.dms.persistence.studyroom.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.studyroom.entity.StudyRoomTimeSlotJpaEntity

@Component
class StudyRoomTimeSlotMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<StudyRoomTimeSlot, StudyRoomTimeSlotJpaEntity> {

    override fun toDomain(entity: StudyRoomTimeSlotJpaEntity?): StudyRoomTimeSlot? {
        return entity?.let {
            StudyRoomTimeSlot(
                id = it.id!!,
                schoolId = it.school!!.id!!,
                startTime = it.startTime,
                endTime = it.endTime
            )
        }
    }

    override fun toEntity(domain: StudyRoomTimeSlot): StudyRoomTimeSlotJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)
        return StudyRoomTimeSlotJpaEntity(
            id = domain.id,
            school = school,
            startTime = domain.startTime,
            endTime = domain.endTime
        )
    }
}

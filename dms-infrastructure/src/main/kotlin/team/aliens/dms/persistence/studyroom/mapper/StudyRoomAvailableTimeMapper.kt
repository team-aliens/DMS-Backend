package team.aliens.dms.persistence.studyroom.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.StudyRoomAvailableTime
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.studyroom.entity.StudyRoomAvailableTimeJpaEntity

@Component
class StudyRoomAvailableTimeMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<StudyRoomAvailableTime, StudyRoomAvailableTimeJpaEntity> {

    override fun toDomain(entity: StudyRoomAvailableTimeJpaEntity?): StudyRoomAvailableTime? {
        return entity?.let {
            StudyRoomAvailableTime(
                schoolId = entity.schoolId,
                startAt = entity.startAt,
                endAt = entity.endAt
            )
        }
    }

    override fun toEntity(domain: StudyRoomAvailableTime): StudyRoomAvailableTimeJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return StudyRoomAvailableTimeJpaEntity(
            schoolId = domain.schoolId,
            school = school,
            startAt = domain.startAt,
            endAt = domain.endAt
        )
    }
}
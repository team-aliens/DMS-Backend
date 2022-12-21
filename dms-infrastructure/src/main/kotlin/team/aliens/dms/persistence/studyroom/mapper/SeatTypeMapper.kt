package team.aliens.dms.persistence.studyroom.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.studyroom.entity.SeatTypeJpaEntity

@Component
class SeatTypeMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<SeatType, SeatTypeJpaEntity>{

    override fun toDomain(entity: SeatTypeJpaEntity?): SeatType? {
        return entity?.let {
            SeatType(
                id = it.id!!,
                schoolId = it.school!!.id!!,
                name = it.name,
                color = it.color
            )
        }
    }

    override fun toEntity(domain: SeatType): SeatTypeJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return SeatTypeJpaEntity(
            id = domain.id,
            school = school,
            name = domain.name,
            color = domain.color
        )
    }
}
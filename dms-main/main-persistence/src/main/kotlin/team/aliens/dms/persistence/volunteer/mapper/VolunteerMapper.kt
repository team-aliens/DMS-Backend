package team.aliens.dms.persistence.volunteer.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.volunteer.entity.VolunteerJpaEntity

@Component
class VolunteerMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<Volunteer, VolunteerJpaEntity> {

    override fun toDomain(entity: VolunteerJpaEntity?): Volunteer? {
        return entity?.let {
            Volunteer(
                id = it.id!!,
                name = it.name,
                score = it.score,
                optionalScore = it.optionalScore,
                maxApplicants = it.maxApplicants,
                availableSex = it.availableSex,
                availableGrade = it.availableGrade,
                schoolId = it.school!!.id!!
            )
        }
    }

    override fun toEntity(domain: Volunteer): VolunteerJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)
        return VolunteerJpaEntity(
            id = domain.id,
            name = domain.name,
            score = domain.score,
            optionalScore = domain.optionalScore,
            maxApplicants = domain.maxApplicants,
            availableSex = domain.availableSex,
            availableGrade = domain.availableGrade,
            school = school
        )
    }
}

package team.aliens.dms.persistence.volunteer.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.volunteer.entity.VolunteerJpaEntity

@Component
class VolunteerMapper() : GenericMapper<Volunteer, VolunteerJpaEntity> {

    override fun toDomain(entity: VolunteerJpaEntity?): Volunteer? {
        return entity?.let {
            Volunteer(
                id = it.id!!,
                name = it.name,
                content = it.content,
                score = it.score,
                optionalScore = it.optionalScore,
                maxApplicants = it.maxApplicants,
                sexCondition = it.sexCondition,
                gradeCondition = it.gradeCondition
            )
        }
    }

    override fun toEntity(domain: Volunteer): VolunteerJpaEntity {
        return VolunteerJpaEntity(
            id = domain.id,
            name = domain.name,
            content = domain.content,
            score = domain.score,
            optionalScore = domain.optionalScore,
            maxApplicants = domain.maxApplicants,
            sexCondition = domain.sexCondition,
            gradeCondition = domain.gradeCondition
        )
    }
}

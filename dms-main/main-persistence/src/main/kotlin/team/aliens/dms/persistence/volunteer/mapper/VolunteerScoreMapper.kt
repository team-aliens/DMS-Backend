package team.aliens.dms.persistence.volunteer.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.model.VolunteerScore
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.volunteer.entity.VolunteerScoreJpaEntity
import team.aliens.dms.persistence.volunteer.repository.VolunteerApplicationJpaRepository

@Component
class VolunteerScoreMapper(
    private val volunteerApplicationRepository: VolunteerApplicationJpaRepository
) : GenericMapper<VolunteerScore, VolunteerScoreJpaEntity> {

    override fun toDomain(entity: VolunteerScoreJpaEntity?): VolunteerScore? {
        return entity?.let {
            VolunteerScore(
                id = it.id!!,
                applicationId = it.volunteerApplication.id!!,
                assignScore = it.assignScore
            )
        }
    }

    override fun toEntity(domain: VolunteerScore): VolunteerScoreJpaEntity {
        val volunteerApplication = volunteerApplicationRepository.findByIdOrNull(domain.applicationId)

        return VolunteerScoreJpaEntity(
            id = domain.id,
            volunteerApplication = volunteerApplication!!,
            assignScore = domain.assignScore
        )
    }
}

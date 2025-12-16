package team.aliens.dms.persistence.volunteer

import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.model.VolunteerScore
import team.aliens.dms.domain.volunteer.spi.VolunteerScorePort
import team.aliens.dms.persistence.volunteer.mapper.VolunteerScoreMapper
import team.aliens.dms.persistence.volunteer.repository.VolunteerScoreRepository
import java.util.UUID

@Component
class VolunteerScorePersistenceAdapter(
    private val volunteerScoreRepository: VolunteerScoreRepository,
    private val volunteerScoreMapper: VolunteerScoreMapper
) : VolunteerScorePort {

    override fun saveVolunteerScore(volunteerScore: VolunteerScore) {
        volunteerScoreRepository.save(
            volunteerScoreMapper.toEntity(volunteerScore)
        )
    }

    override fun queryExistsByApplicationId(applicationId: UUID): Boolean {
        return volunteerScoreRepository.existsByVolunteerApplicationId(applicationId)
    }
}

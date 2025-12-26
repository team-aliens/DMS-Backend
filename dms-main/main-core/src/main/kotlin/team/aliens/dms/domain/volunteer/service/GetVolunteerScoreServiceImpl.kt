package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationNotFoundException
import team.aliens.dms.domain.volunteer.model.VolunteerScore
import team.aliens.dms.domain.volunteer.spi.QueryVolunteerScorePort
import java.util.UUID

@Service
class GetVolunteerScoreServiceImpl(
    private val queryVolunteerScorePort: QueryVolunteerScorePort
) : GetVolunteerScoreService  {

    override fun getVolunteerApplicationScoreById(applicationId: UUID): VolunteerScore {
        return queryVolunteerScorePort.queryScoreByApplicationId(applicationId)
            ?: throw VolunteerApplicationNotFoundException
    }
}

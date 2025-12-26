package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.domain.volunteer.model.VolunteerScore
import java.util.UUID

interface GetVolunteerScoreService {

    fun getVolunteerApplicationScoreById(applicationId: UUID): VolunteerScore
}

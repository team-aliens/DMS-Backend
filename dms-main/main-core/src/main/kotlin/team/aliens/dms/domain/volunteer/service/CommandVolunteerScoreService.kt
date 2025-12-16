package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import java.util.UUID

interface CommandVolunteerScoreService {

    fun createVolunteerScore(volunteerApplication: VolunteerApplication, volunteer: Volunteer)

    fun updateVolunteerScore(applicationId: UUID, updateScore: Int)
}

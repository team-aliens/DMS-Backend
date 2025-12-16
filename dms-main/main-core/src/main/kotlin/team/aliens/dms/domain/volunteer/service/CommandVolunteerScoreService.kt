package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication

interface CommandVolunteerScoreService {

    fun createVolunteerScore(volunteerApplication: VolunteerApplication, volunteer: Volunteer)
}

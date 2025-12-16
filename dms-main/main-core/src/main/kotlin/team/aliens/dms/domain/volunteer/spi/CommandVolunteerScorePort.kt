package team.aliens.dms.domain.volunteer.spi

import team.aliens.dms.domain.volunteer.model.VolunteerScore

interface CommandVolunteerScorePort {

    fun saveVolunteerScore(volunteerScore: VolunteerScore)
}

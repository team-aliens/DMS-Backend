package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.domain.volunteer.model.VolunteerApplication

interface CommandVolunteerService {

    fun saveVolunteerApplication(volunteerApplication: VolunteerApplication): VolunteerApplication

    fun deleteVolunteerApplication(volunteerApplication: VolunteerApplication)
}

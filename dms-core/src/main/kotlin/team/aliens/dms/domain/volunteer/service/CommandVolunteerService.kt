package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication

interface CommandVolunteerService {

    fun saveVolunteerApplication(volunteerApplication: VolunteerApplication): VolunteerApplication

    fun deleteVolunteerApplication(volunteerApplication: VolunteerApplication)

    fun saveVolunteer(volunteer: Volunteer): Volunteer
}

package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import java.util.UUID

interface CommandVolunteerService {

    fun saveVolunteerApplication(volunteerApplication: VolunteerApplication): VolunteerApplication

    fun deleteVolunteerApplication(volunteerApplication: VolunteerApplication)

    fun deleteAllVolunteerApplicationsByVolunteerId(volunteerId: UUID)

    fun saveVolunteer(volunteer: Volunteer): Volunteer

    fun deleteVolunteer(volunteer: Volunteer)

    fun createVolunteerScore(volunteerApplication: VolunteerApplication, volunteer: Volunteer)

    fun updateVolunteerScore(applicationId: UUID, updateScore: Int)

    fun deleteAllVolunteerScores()
}

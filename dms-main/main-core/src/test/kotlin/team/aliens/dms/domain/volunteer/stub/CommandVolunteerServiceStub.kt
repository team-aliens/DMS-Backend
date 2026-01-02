package team.aliens.dms.domain.volunteer.stub

import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.service.CommandVolunteerService
import java.util.UUID

abstract class CommandVolunteerServiceStub : CommandVolunteerService {
    override fun saveVolunteerApplication(volunteerApplication: VolunteerApplication): VolunteerApplication =
        throw UnsupportedOperationException()

    override fun deleteVolunteerApplication(volunteerApplication: VolunteerApplication) =
        throw UnsupportedOperationException()

    override fun deleteAllVolunteerApplicationsByVolunteerId(volunteerId: UUID) =
        throw UnsupportedOperationException()

    override fun saveVolunteer(volunteer: Volunteer): Volunteer =
        throw UnsupportedOperationException()

    override fun deleteVolunteer(volunteer: Volunteer) =
        throw UnsupportedOperationException()

    override fun createVolunteerScore(volunteerApplication: VolunteerApplication, volunteer: Volunteer) =
        throw UnsupportedOperationException()

    override fun updateVolunteerScore(applicationId: UUID, updateScore: Int) =
        throw UnsupportedOperationException()

    override fun deleteAllVolunteerScores() =
        throw UnsupportedOperationException()
}
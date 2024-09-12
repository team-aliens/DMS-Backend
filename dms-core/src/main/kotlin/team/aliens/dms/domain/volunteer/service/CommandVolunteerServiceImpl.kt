package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.spi.CommandVolunteerApplicationPort
import team.aliens.dms.domain.volunteer.spi.CommandVolunteerPort

@Service
class CommandVolunteerServiceImpl(
    private val commandVolunteerApplicationPort: CommandVolunteerApplicationPort,
    private val commandVolunteerPort: CommandVolunteerPort
) : CommandVolunteerService {

    override fun saveVolunteerApplication(volunteerApplication: VolunteerApplication): VolunteerApplication =
        commandVolunteerApplicationPort.saveVolunteerApplication(volunteerApplication)

    override fun deleteVolunteerApplication(volunteerApplication: VolunteerApplication) {
        commandVolunteerApplicationPort.deleteVolunteerApplication(volunteerApplication)
    }

    override fun saveVolunteer(volunteer: Volunteer): Volunteer =
        commandVolunteerPort.saveVolunteer(volunteer)
}

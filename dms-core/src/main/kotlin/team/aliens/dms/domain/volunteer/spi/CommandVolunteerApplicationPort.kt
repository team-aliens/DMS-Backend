package team.aliens.dms.domain.volunteer.spi

import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import java.util.UUID

interface CommandVolunteerApplicationPort {

    fun saveVolunteerApplication(volunteerApplication: VolunteerApplication): VolunteerApplication

    fun deleteVolunteerApplication(volunteerApplication: VolunteerApplication)

    fun deleteAllVolunteerApplicationsByVolunteerId(volunteerId: UUID)
}

package team.aliens.dms.domain.volunteer.spi

import team.aliens.dms.domain.volunteer.model.Volunteer
import java.util.UUID

interface CommandVolunteerPort {

    fun saveVolunteer(volunteer: Volunteer): Volunteer

    fun deleteVolunteer(volunteer: Volunteer)
}

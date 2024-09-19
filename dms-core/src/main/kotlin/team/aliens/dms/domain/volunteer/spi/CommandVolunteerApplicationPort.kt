package team.aliens.dms.domain.volunteer.spi

import team.aliens.dms.domain.volunteer.model.VolunteerApplication

interface CommandVolunteerApplicationPort {

    fun saveVolunteerApplication(volunteerApplication: VolunteerApplication): VolunteerApplication

    fun deleteVolunteerApplication(volunteerApplication: VolunteerApplication)
}

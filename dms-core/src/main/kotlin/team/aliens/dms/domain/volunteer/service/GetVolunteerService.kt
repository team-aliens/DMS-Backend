package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import java.util.UUID

interface GetVolunteerService {

    fun getVolunteerApplicationById(volunteerApplicationId: UUID): VolunteerApplication

    fun getVolunteerById(volunteerId: UUID): Volunteer
}

package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import java.util.UUID

interface GetVolunteerService {

    fun getVolunteerApplicationById(volunteerApplicationId: UUID): VolunteerApplication

}

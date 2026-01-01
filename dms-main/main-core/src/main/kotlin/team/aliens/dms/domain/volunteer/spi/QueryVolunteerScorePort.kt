package team.aliens.dms.domain.volunteer.spi

import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.model.VolunteerScore
import java.util.UUID

interface QueryVolunteerScorePort {

    fun queryExistsByApplicationId(applicationId: UUID): Boolean

    fun queryVolunteerById(volunteerId: UUID): Volunteer?

    fun queryVolunteerApplicationById(applicationId: UUID): VolunteerApplication?

    fun queryScoreByApplicationId(applicationId: UUID): VolunteerScore?
}

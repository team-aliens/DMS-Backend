package team.aliens.dms.domain.volunteer.spi

import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.model.VolunteerScore
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerScoreWithStudentVO
import java.util.UUID

interface QueryVolunteerScorePort {

    fun queryExistsByApplicationId(applicationId: UUID): Boolean

    fun queryVolunteerById(volunteerId: UUID): Volunteer?

    fun queryVolunteerApplicationById(applicationId: UUID): VolunteerApplication?

    fun queryAllVolunteerScoresWithStudentVO(): List<VolunteerScoreWithStudentVO>

    fun queryScoreByApplicationId(applicationId: UUID): VolunteerScore?
}

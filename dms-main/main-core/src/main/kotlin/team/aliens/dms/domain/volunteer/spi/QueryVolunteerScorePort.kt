package team.aliens.dms.domain.volunteer.spi

import java.util.UUID

interface QueryVolunteerScorePort {

    fun queryExistsByApplicationId(applicationId: UUID): Boolean
}

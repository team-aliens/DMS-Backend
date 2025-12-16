package team.aliens.dms.domain.volunteer.spi

import team.aliens.dms.domain.volunteer.model.VolunteerScore
import java.util.UUID

interface CommandVolunteerScorePort {

    fun saveVolunteerScore(volunteerScore: VolunteerScore)

    fun updateVolunteerScore(applicationId: UUID, updateScore: Int)
}

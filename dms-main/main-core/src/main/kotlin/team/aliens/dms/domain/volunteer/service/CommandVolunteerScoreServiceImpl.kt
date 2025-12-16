package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationAlreadyAssigned
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.model.VolunteerScore
import team.aliens.dms.domain.volunteer.spi.CommandVolunteerScorePort
import team.aliens.dms.domain.volunteer.spi.QueryVolunteerScorePort

@Service
class CommandVolunteerScoreServiceImpl(
    private val commandVolunteerScorePort: CommandVolunteerScorePort,
    private val queryVolunteerScorePort: QueryVolunteerScorePort
) : CommandVolunteerScoreService {

    override fun createVolunteerScore(
        volunteerApplication: VolunteerApplication, volunteer: Volunteer
    ) {
        volunteerApplication.checkIsApproved()

        if (queryVolunteerScorePort.queryExistsByApplicationId(volunteerApplication.id)) {
            throw VolunteerApplicationAlreadyAssigned
        }

        val volunteerScore = VolunteerScore(
            applicationId = volunteerApplication.id,
            assignScore = volunteer.maxScore
        )

        commandVolunteerScorePort.saveVolunteerScore(volunteerScore)
    }
}

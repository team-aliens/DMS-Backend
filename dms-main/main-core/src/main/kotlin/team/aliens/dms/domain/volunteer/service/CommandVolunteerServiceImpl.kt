package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationAlreadyAssigned
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationNotFoundException
import team.aliens.dms.domain.volunteer.exception.VolunteerInvalidScoreRangeException
import team.aliens.dms.domain.volunteer.exception.VolunteerNotFoundException
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.model.VolunteerScore
import team.aliens.dms.domain.volunteer.spi.CommandVolunteerApplicationPort
import team.aliens.dms.domain.volunteer.spi.CommandVolunteerPort
import team.aliens.dms.domain.volunteer.spi.CommandVolunteerScorePort
import team.aliens.dms.domain.volunteer.spi.QueryVolunteerScorePort
import java.util.UUID

@Service
class CommandVolunteerServiceImpl(
    private val commandVolunteerApplicationPort: CommandVolunteerApplicationPort,
    private val commandVolunteerPort: CommandVolunteerPort,
    private val commandVolunteerScorePort: CommandVolunteerScorePort,
    private val queryVolunteerScorePort: QueryVolunteerScorePort
) : CommandVolunteerService {

    override fun saveVolunteerApplication(volunteerApplication: VolunteerApplication): VolunteerApplication =
        commandVolunteerApplicationPort.saveVolunteerApplication(volunteerApplication)

    override fun deleteVolunteerApplication(volunteerApplication: VolunteerApplication) {
        commandVolunteerApplicationPort.deleteVolunteerApplication(volunteerApplication)
    }

    override fun deleteAllVolunteerApplicationsByVolunteerId(volunteerId: UUID) {
        commandVolunteerApplicationPort.deleteAllVolunteerApplicationsByVolunteerId(volunteerId)
    }

    override fun saveVolunteer(volunteer: Volunteer): Volunteer =
        commandVolunteerPort.saveVolunteer(volunteer)

    override fun deleteVolunteer(volunteer: Volunteer) {
        commandVolunteerPort.deleteVolunteer(volunteer)
    }

    override fun createVolunteerScore(
        volunteerApplication: VolunteerApplication,
        volunteer: Volunteer
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

    override fun updateVolunteerScore(applicationId: UUID, updateScore: Int) {
        val volunteerApplication = queryVolunteerScorePort.queryVolunteerApplicationById(applicationId)
            ?: throw VolunteerApplicationNotFoundException

        val volunteer = queryVolunteerScorePort.queryVolunteerById(volunteerApplication.volunteerId)
            ?: throw VolunteerNotFoundException

        if (updateScore < volunteer.minScore || updateScore > volunteer.maxScore) {
            throw VolunteerInvalidScoreRangeException
        }

        commandVolunteerScorePort.updateVolunteerScore(applicationId, updateScore)
    }

    override fun deleteAllVolunteerScores() {
        commandVolunteerScorePort.deleteAllVolunteerScores()
    }
}

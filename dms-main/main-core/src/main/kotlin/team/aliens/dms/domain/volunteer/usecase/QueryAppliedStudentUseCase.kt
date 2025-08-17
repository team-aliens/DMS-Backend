package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.volunteer.dto.response.VolunteerApplicantResponse
import team.aliens.dms.domain.volunteer.dto.response.VolunteerApplicantsResponse
import team.aliens.dms.domain.volunteer.service.VolunteerService
import java.util.UUID

@ReadOnlyUseCase
class QueryAppliedStudentUseCase(
    private val volunteerService: VolunteerService
) {

    fun execute(volunteerId: UUID): VolunteerApplicantsResponse {
        val currentVolunteer = volunteerService.getVolunteerById(volunteerId)

        val applicants = volunteerService.getAllApplicantsByVolunteerId(currentVolunteer.id)
            .map { VolunteerApplicantResponse.of(it) }

        return VolunteerApplicantsResponse(applicants)
    }
}

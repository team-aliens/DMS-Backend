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

        val applicants = volunteerService.getAllApplicantsByVolunteerId(volunteerId)
            .map { VolunteerApplicantResponse.from(it) }

        return VolunteerApplicantsResponse(applicants)
    }
}

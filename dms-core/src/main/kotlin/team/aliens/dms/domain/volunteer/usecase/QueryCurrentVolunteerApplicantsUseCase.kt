package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.volunteer.dto.response.CurrentVolunteerApplicantResponse
import team.aliens.dms.domain.volunteer.dto.response.CurrentVolunteerApplicantsResponse
import team.aliens.dms.domain.volunteer.service.VolunteerService

@ReadOnlyUseCase
class QueryCurrentVolunteerApplicantsUseCase(
    private val securityService: SecurityService,
    private val volunteerService: VolunteerService
) {

    fun execute(): CurrentVolunteerApplicantsResponse {
        val schoolId = securityService.getCurrentSchoolId()

        val currentApplicantsGroup = volunteerService.getAllApplicantsBySchoolIdGroupByVolunteer(schoolId)

        return CurrentVolunteerApplicantsResponse(
            currentApplicantsGroup
                .map { CurrentVolunteerApplicantResponse.of(it) }
        )
    }
}

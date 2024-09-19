package team.aliens.dms.persistence.volunteer.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.volunteer.spi.vo.CurrentVolunteerApplicantVO

class QueryCurrentVolunteerApplicantVO @QueryProjection constructor(
    volunteerName: String,
    applicants: List<QueryVolunteerApplicantVO>
) : CurrentVolunteerApplicantVO(
    volunteerName = volunteerName,
    applicants = applicants
)

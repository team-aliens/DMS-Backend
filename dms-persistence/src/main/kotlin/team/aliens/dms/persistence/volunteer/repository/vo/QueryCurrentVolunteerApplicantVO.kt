package team.aliens.dms.persistence.volunteer.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.AvailableGrade
import team.aliens.dms.domain.volunteer.spi.vo.CurrentVolunteerApplicantVO

class QueryCurrentVolunteerApplicantVO @QueryProjection constructor(
    volunteerName: String,
    availableSex: Sex,
    availableGrade: AvailableGrade,
    applicants: List<QueryVolunteerApplicantVO>
) : CurrentVolunteerApplicantVO(
    volunteerName = volunteerName,
    availableSex = availableSex,
    availableGrade = availableGrade,
    applicants = applicants
)

package team.aliens.dms.persistence.volunteer.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.AvailableGrade
import team.aliens.dms.domain.volunteer.spi.vo.CurrentVolunteerApplicantVO
import java.util.UUID

class QueryCurrentVolunteerApplicantVO @QueryProjection constructor(
    volunteerName: String,
    availableSex: Sex,
    availableGrade: AvailableGrade,
    currentApplicants: List<UUID>,
    maxApplicants: Int,
    applicants: List<QueryVolunteerApplicantVO>
) : CurrentVolunteerApplicantVO(
    volunteerName = volunteerName,
    availableSex = availableSex,
    availableGrade = availableGrade,
    currentApplicants = currentApplicants.size,
    maxApplicants = maxApplicants,
    applicants = applicants
)

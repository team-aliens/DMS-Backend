package team.aliens.dms.persistence.volunteer.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.AvailableGrade
import team.aliens.dms.domain.volunteer.model.VolunteerApplicationStatus
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerWithCurrentApplicantVO
import java.util.UUID

class QueryVolunteerWithCurrentApplicantVO @QueryProjection constructor(
    id: UUID,
    name: String,
    maxScore: Int,
    minScore: Int,
    currentApplicants: Set<UUID>,
    maxApplicants: Int,
    availableSex: Sex,
    availableGrade: AvailableGrade,
    schoolId: UUID,
    status: Boolean?
) : VolunteerWithCurrentApplicantVO(
    id = id,
    name = name,
    maxScore = maxScore,
    minScore = minScore,
    currentApplicants = currentApplicants.size,
    maxApplicants = maxApplicants,
    availableSex = availableSex,
    availableGrade = availableGrade,
    schoolId = schoolId,
    status = VolunteerApplicationStatus.of(status)
)

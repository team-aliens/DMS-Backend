package team.aliens.dms.persistence.volunteer.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerApplicantVO
import java.util.UUID

class QueryVolunteerApplicantVO @QueryProjection constructor(
    id: UUID,
    grade: Int,
    classNumber: Int,
    number: Int,
    name: String
) : VolunteerApplicantVO(
    id = id,
    grade = grade,
    classRoom = classNumber,
    number = number,
    name = name
)

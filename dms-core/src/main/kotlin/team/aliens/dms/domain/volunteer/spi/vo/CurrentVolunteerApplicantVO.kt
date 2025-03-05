package team.aliens.dms.domain.volunteer.spi.vo

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.AvailableGrade
import java.util.UUID

open class CurrentVolunteerApplicantVO(
    val id: UUID,
    val name: String,
    val availableSex: Sex,
    val availableGrade: AvailableGrade,
    val currentApplicants: Int,
    val maxApplicants: Int,
    val applicants: List<VolunteerApplicantVO>
)

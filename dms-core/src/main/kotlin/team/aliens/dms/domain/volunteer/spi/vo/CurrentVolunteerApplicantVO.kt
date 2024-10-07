package team.aliens.dms.domain.volunteer.spi.vo

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.AvailableGrade

open class CurrentVolunteerApplicantVO(
    val volunteerName: String,
    val availableSex: Sex,
    val availableGrade: AvailableGrade,
    val currentApplicants: Int,
    val maxApplicants: Int,
    val applicants: List<VolunteerApplicantVO>
)

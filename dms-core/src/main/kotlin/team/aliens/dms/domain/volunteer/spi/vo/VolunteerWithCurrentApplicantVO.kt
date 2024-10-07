package team.aliens.dms.domain.volunteer.spi.vo

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.AvailableGrade
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplicationStatus
import java.util.UUID

open class VolunteerWithCurrentApplicantVO(
    val id: UUID,
    val name: String,
    val content: String,
    val score: Int,
    val optionalScore: Int,
    val currentApplicants: Int,
    val maxApplicants: Int,
    val availableSex: Sex,
    val availableGrade: AvailableGrade,
    val schoolId: UUID,
    val status: VolunteerApplicationStatus
) {

    fun toVolunteer() = Volunteer(
        id = id,
        name = name,
        content = content,
        score = score,
        optionalScore = optionalScore,
        maxApplicants = maxApplicants,
        availableSex = availableSex,
        availableGrade = availableGrade,
        schoolId = schoolId
    )
}

package team.aliens.dms.domain.volunteer.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.AvailableGrade

data class UpdateVolunteerWebRequest(

    @field:NotBlank
    val name: String,

    @field:NotNull
    val availableSex: Sex,

    @field:NotNull
    val availableGrade: AvailableGrade,

    @field:NotNull
    val score: Int?,

    @field:NotNull
    val optionalScore: Int?,

    @field:NotNull
    val maxApplicants: Int?
)

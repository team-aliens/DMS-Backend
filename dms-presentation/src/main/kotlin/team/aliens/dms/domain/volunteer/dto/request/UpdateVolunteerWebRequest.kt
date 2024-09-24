package team.aliens.dms.domain.volunteer.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.AvailableGrade

data class UpdateVolunteerWebRequest(

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val content: String,

    @field:Size(min = 1, max = 6)
    @field:NotBlank
    val availableSex: Sex,

    @field:Size(min = 1, max = 16)
    @field:NotBlank
    val availableGrade: AvailableGrade,

    @field:NotNull
    val score: Int,

    val optionalScore: Int = 0,

    @field:NotNull
    val maxApplicants: Int
)

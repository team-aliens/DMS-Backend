package team.aliens.dms.domain.volunteer.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateVolunteerWebRequest(

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val content: String,

    @field:Size(min = 1, max = 6)
    @field:NotBlank
    val sexCondition: String,

    @field:Size(min = 1, max = 14)
    @field:NotBlank
    val gradeCondition: String,

    @field:NotNull
    val score: Int,

    val optionalScore: Int = 0,

    @field:NotNull
    val maxApplicants: Int
)

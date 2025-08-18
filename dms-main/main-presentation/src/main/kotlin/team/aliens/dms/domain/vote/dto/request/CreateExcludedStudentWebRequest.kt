package team.aliens.dms.domain.vote.dto.request

import jakarta.validation.constraints.NotBlank

data class CreateExcludedStudentWebRequest(
    @field:NotBlank
    val gcn: String
)

package team.aliens.dms.domain.user.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import team.aliens.dms.domain.manager.dto.request.Password

data class UpdateUserPasswordWebRequest(

    @field:NotBlank
    @field:Size(min = 8, max = 20)
    val currentPassword: String,

    @field:NotBlank
    @field:Size(min = 8, max = 20)
    @field:Pattern(
        regexp = Password.PATTERN,
        message = Password.MESSAGE
    )
    val newPassword: String

)

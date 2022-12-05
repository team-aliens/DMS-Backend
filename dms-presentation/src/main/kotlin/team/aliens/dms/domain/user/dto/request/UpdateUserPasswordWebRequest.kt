package team.aliens.dms.domain.user.dto.request

import team.aliens.dms.domain.manager.dto.request.Password
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class UpdateUserPasswordWebRequest(

    @field:NotBlank
    val currentPassword: String,

    @field:NotBlank
    @field:Pattern(
        regexp = Password.PATTERN,
        message = Password.MESSAGE
    )
    val newPassword: String

)

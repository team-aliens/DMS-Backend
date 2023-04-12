package team.aliens.dms.domain.user.dto.request

import team.aliens.dms.domain.manager.dto.request.Password
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

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

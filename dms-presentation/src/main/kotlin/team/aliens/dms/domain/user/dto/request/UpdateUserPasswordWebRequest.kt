package team.aliens.dms.domain.user.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import team.aliens.dms.domain.manager.dto.request.Password
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class UpdateUserPasswordWebRequest(

    @field:NotBlank
    @field:JsonFormat(shape = JsonFormat.Shape.STRING)
    val oldPassword: Password,

    @field:NotBlank
    @field:Pattern(regexp = Password.PATTERN)
    @field:JsonFormat(shape = JsonFormat.Shape.STRING)
    val newPassword: Password

)

package team.aliens.dms.domain.user

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.user.dto.UpdateUserPasswordRequest
import team.aliens.dms.domain.user.usecase.UpdateUserPasswordUseCase
import team.aliens.dms.domain.user.usecase.UserPasswordCompareUseCase
import team.aliens.dms.domain.user.dto.request.UpdateUserPasswordWebRequest
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Validated
@RequestMapping("/users")
@RestController
class UserWebAdapter(
    private val userPasswordCompareUseCase: UserPasswordCompareUseCase,
    private val updateUserPasswordUseCase: UpdateUserPasswordUseCase
) {

    @GetMapping("/password")
    fun passwordCompare(@RequestParam @NotBlank password: String) {
        userPasswordCompareUseCase.execute(password)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/password")
    fun updatePassword(@RequestBody @Valid webRequest: UpdateUserPasswordWebRequest) {
        val request = UpdateUserPasswordRequest(
            currentPassword = webRequest.currentPassword,
            newPassword = webRequest.newPassword
        )

        updateUserPasswordUseCase.execute(request)
    }
}
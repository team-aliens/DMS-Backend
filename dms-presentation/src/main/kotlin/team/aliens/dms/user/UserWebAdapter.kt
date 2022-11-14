package team.aliens.dms.user

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.user.dto.UpdateUserPasswordRequest
import team.aliens.dms.domain.user.usecase.UpdateUserPasswordUseCase
import team.aliens.dms.user.dto.request.UpdateUserPasswordWebRequest

@RequestMapping("/users")
@RestController
class UserWebAdapter(
    private val updateUserPasswordUseCase: UpdateUserPasswordUseCase
) {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/password")
    fun updatePassword(webRequest: UpdateUserPasswordWebRequest) {
        val request = UpdateUserPasswordRequest(
            oldPassword = webRequest.oldPassword.value,
            newPassword = webRequest.newPassword.value
        )

        updateUserPasswordUseCase.execute(request)
    }
}
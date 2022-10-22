package team.aliens.dms.auth

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import team.aliens.dms.auth.dto.request.CertifyEmailCodeWebRequest
import team.aliens.dms.auth.dto.request.CertifyEmailWebRequest
import team.aliens.dms.auth.dto.request.SendEmailCodeWebRequest
import team.aliens.dms.auth.dto.response.CheckAccountIdExistenceResponse
import team.aliens.dms.domain.auth.dto.CertifyEmailCodeRequest
import team.aliens.dms.domain.auth.dto.CertifyEmailRequest
import team.aliens.dms.domain.auth.dto.SendEmailCodeRequest
import team.aliens.dms.domain.auth.usecase.CertifyEmailCodeUseCase
import team.aliens.dms.domain.auth.usecase.CertifyEmailUseCase
import team.aliens.dms.domain.auth.usecase.CheckAccountIdExistenceUseCase
import team.aliens.dms.domain.auth.usecase.SendEmailCodeUseCase
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Validated
@RequestMapping("/auth")
@RestController
class AuthWebAdapter(
    private val sendEmailCodeUseCase: SendEmailCodeUseCase,
    private val certifyEmailCodeUseCase: CertifyEmailCodeUseCase,
    private val certifyEmailUseCase: CertifyEmailUseCase,
    private val checkAccountIdExistenceUseCase: CheckAccountIdExistenceUseCase
) {

    @GetMapping("/code")
    fun certifyEmailCode(@ModelAttribute request: CertifyEmailCodeWebRequest) {
        certifyEmailCodeUseCase.execute(
            CertifyEmailCodeRequest(
                email = request.email,
                authCode = request.authCode,
                type = request.type.toString()
            )
        )
    }

    @PostMapping("/code")
    fun sendEmailCode(@RequestBody @Valid request: SendEmailCodeWebRequest) {
        sendEmailCodeUseCase.execute(
            SendEmailCodeRequest(
                email = request.email,
                type = request.type.toString()
            )
        )
    }

    @GetMapping("/email")
    fun certifyEmail(@ModelAttribute request: CertifyEmailWebRequest) {
        certifyEmailUseCase.execute(
            CertifyEmailRequest(
                accountId = request.accountId,
                email = request.email
            )
        )
    }

    @GetMapping("/account-id")
    fun checkAccountIdExistence(@RequestParam @NotBlank accountId: String): CheckAccountIdExistenceResponse {
        return CheckAccountIdExistenceResponse(checkAccountIdExistenceUseCase.execute(accountId))
    }
}
package team.aliens.dms.auth

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import team.aliens.dms.auth.dto.request.CertifyEmailCodeWebRequest
import team.aliens.dms.auth.dto.request.CertifyEmailWebRequest
import team.aliens.dms.auth.dto.request.SendEmailCodeWebRequest
import team.aliens.dms.auth.dto.request.SignInWebRequest
import team.aliens.dms.auth.dto.response.CheckAccountIdExistenceResponse
import team.aliens.dms.domain.auth.dto.*
import team.aliens.dms.domain.auth.usecase.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Validated
@RequestMapping("/auth")
@RestController
class AuthWebAdapter(
    private val sendEmailCodeUseCase: SendEmailCodeUseCase,
    private val certifyEmailCodeUseCase: CertifyEmailCodeUseCase,
    private val certifyEmailUseCase: CertifyEmailUseCase,
    private val checkAccountIdExistenceUseCase: CheckAccountIdExistenceUseCase,
    private val reissueTokenUseCase: ReissueTokenUseCase,
    private val signInUseCase: SignInUseCase
) {

    @PostMapping("/tokens")
    fun singIn(@RequestBody @Valid request: team.aliens.dms.auth.dto.request.SignInWebRequest): SignInResponse {
        return signInUseCase.execute(
            SignInRequest(
                accountId = request.accountId,
                password = request.password
            )
        )
    }

    @GetMapping("/code")
    fun certifyEmailCode(@ModelAttribute request: team.aliens.dms.auth.dto.request.CertifyEmailCodeWebRequest) {
        certifyEmailCodeUseCase.execute(
            CertifyEmailCodeRequest(
                email = request.email,
                authCode = request.authCode,
                type = request.type.toString()
            )
        )
    }

    @PostMapping("/code")
    fun sendEmailCode(@RequestBody @Valid request: team.aliens.dms.auth.dto.request.SendEmailCodeWebRequest) {
        sendEmailCodeUseCase.execute(
            SendEmailCodeRequest(
                email = request.email,
                type = request.type.toString()
            )
        )
    }

    @GetMapping("/email")
    fun certifyEmail(@ModelAttribute request: team.aliens.dms.auth.dto.request.CertifyEmailWebRequest) {
        certifyEmailUseCase.execute(
            CertifyEmailRequest(
                accountId = request.accountId,
                email = request.email
            )
        )
    }

    @PutMapping("/reissue")
    fun reissueToken(@RequestHeader("refresh-token") refreshToken: String): ReissueResponse {
        return reissueTokenUseCase.execute(refreshToken)
    }

    @GetMapping("/account-id")
    fun checkAccountIdExistence(@RequestParam @NotBlank accountId: String): team.aliens.dms.auth.dto.response.CheckAccountIdExistenceResponse {
        return team.aliens.dms.auth.dto.response.CheckAccountIdExistenceResponse(
            checkAccountIdExistenceUseCase.execute(
                accountId
            )
        )
    }
}
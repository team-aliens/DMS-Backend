package team.aliens.dms.auth

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.auth.dto.request.CertifyEmailCodeWebRequest
import team.aliens.dms.auth.dto.request.CertifyEmailWebRequest
import team.aliens.dms.auth.dto.request.SendEmailCodeWebRequest
import team.aliens.dms.auth.dto.request.SignInWebRequest
import team.aliens.dms.auth.dto.response.CheckAccountIdExistenceResponse
import team.aliens.dms.domain.auth.dto.CertifyEmailCodeRequest
import team.aliens.dms.domain.auth.dto.CertifyEmailRequest
import team.aliens.dms.domain.auth.dto.ReissueResponse
import team.aliens.dms.domain.auth.dto.SendEmailCodeRequest
import team.aliens.dms.domain.auth.dto.SignInRequest
import team.aliens.dms.domain.auth.dto.SignInResponse
import team.aliens.dms.domain.auth.usecase.CertifyEmailCodeUseCase
import team.aliens.dms.domain.auth.usecase.CertifyEmailUseCase
import team.aliens.dms.domain.auth.usecase.CheckAccountIdExistenceUseCase
import team.aliens.dms.domain.auth.usecase.ReissueTokenUseCase
import team.aliens.dms.domain.auth.usecase.SendEmailCodeUseCase
import team.aliens.dms.domain.auth.usecase.SignInUseCase
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
    fun singIn(@RequestBody @Valid request: SignInWebRequest): SignInResponse {
        return signInUseCase.execute(
            SignInRequest(
                accountId = request.accountId,
                password = request.password
            )
        )
    }

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

    @PutMapping("/reissue")
    fun reissueToken(@RequestHeader("refresh-token") refreshToken: String): ReissueResponse {
        return reissueTokenUseCase.execute(refreshToken)
    }

    @GetMapping("/account-id")
    fun checkAccountIdExistence(@RequestParam @NotBlank accountId: String): CheckAccountIdExistenceResponse {
        return CheckAccountIdExistenceResponse(
            checkAccountIdExistenceUseCase.execute(accountId)
        )
    }
}
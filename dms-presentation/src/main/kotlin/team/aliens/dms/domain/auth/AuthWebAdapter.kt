package team.aliens.dms.domain.auth

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.auth.dto.CertifyEmailCodeRequest
import team.aliens.dms.domain.auth.dto.CertifyEmailRequest
import team.aliens.dms.domain.auth.dto.SendEmailCodeRequest
import team.aliens.dms.domain.auth.dto.SignInRequest
import team.aliens.dms.domain.auth.dto.TokenFeatureResponse
import team.aliens.dms.domain.auth.dto.request.SendEmailCodeWebRequest
import team.aliens.dms.domain.auth.dto.request.SignInWebRequest
import team.aliens.dms.domain.auth.dto.request.WebEmailType
import team.aliens.dms.domain.auth.dto.response.CheckAccountIdExistenceResponse
import team.aliens.dms.domain.auth.usecase.CertifyEmailCodeUseCase
import team.aliens.dms.domain.auth.usecase.CertifyEmailUseCase
import team.aliens.dms.domain.auth.usecase.CheckAccountIdExistenceUseCase
import team.aliens.dms.domain.auth.usecase.ReissueTokenUseCase
import team.aliens.dms.domain.auth.usecase.SendEmailCodeUseCase
import team.aliens.dms.domain.auth.usecase.SignInUseCase

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
    fun singIn(@RequestBody @Valid request: SignInWebRequest): TokenFeatureResponse {
        return signInUseCase.execute(
            SignInRequest(
                accountId = request.accountId,
                password = request.password,
                deviceToken = request.deviceToken
            )
        )
    }

    @GetMapping("/code")
    fun certifyEmailCode(
        @RequestParam @Email @NotBlank email: String,
        @RequestParam("auth_code") @Length(min = 6, max = 6) @NotBlank authCode: String,
        @RequestParam @NotNull type: WebEmailType
    ) {
        certifyEmailCodeUseCase.execute(
            CertifyEmailCodeRequest(
                email = email,
                authCode = authCode,
                type = type.name
            )
        )
    }

    @PostMapping("/code")
    fun sendEmailCode(@RequestBody @Valid request: SendEmailCodeWebRequest) {
        sendEmailCodeUseCase.execute(
            SendEmailCodeRequest(
                email = request.email,
                type = request.type.name
            )
        )
    }

    @GetMapping("/email")
    fun certifyEmail(
        @RequestParam("account_id") @NotBlank accountId: String,
        @RequestParam @Email @NotBlank email: String
    ) {
        certifyEmailUseCase.execute(
            CertifyEmailRequest(
                accountId = accountId,
                email = email
            )
        )
    }

    @PutMapping("/reissue")
    fun reissueToken(@RequestHeader("refresh-token") @NotBlank refreshToken: String): TokenFeatureResponse {
        return reissueTokenUseCase.execute(refreshToken)
    }

    @GetMapping("/account-id")
    fun checkAccountIdExistence(
        @RequestParam("account_id") @NotBlank accountId: String
    ): CheckAccountIdExistenceResponse {
        return CheckAccountIdExistenceResponse(
            checkAccountIdExistenceUseCase.execute(accountId)
        )
    }
}

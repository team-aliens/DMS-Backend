package team.aliens.dms.auth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.auth.dto.request.CertifyEmailCodeWebRequest
import team.aliens.dms.auth.dto.request.CertifyEmailWebRequest
import team.aliens.dms.auth.dto.request.SendEmailCodeWebRequest
import team.aliens.dms.domain.auth.dto.CertifyEmailCodeRequest
import team.aliens.dms.domain.auth.dto.CertifyEmailRequest
import team.aliens.dms.domain.auth.dto.SendEmailCodeRequest
import team.aliens.dms.domain.auth.usecase.CertifyEmailCodeUseCase
import team.aliens.dms.domain.auth.usecase.CertifyEmailUseCase
import team.aliens.dms.domain.auth.usecase.SendEmailCodeUseCase

@RequestMapping("/auth")
@RestController
class AuthController(
    private val sendEmailCodeUseCase: SendEmailCodeUseCase,
    private val certifyEmailCodeUseCase: CertifyEmailCodeUseCase,
    private val certifyEmailUseCase: CertifyEmailUseCase
) {

    @GetMapping("/code")
    fun certifyEmailCode(@RequestBody request: CertifyEmailCodeWebRequest) {
        certifyEmailCodeUseCase.execute(
            CertifyEmailCodeRequest(
                request.email,
                request.authCode,
                request.type.toString()
            )
        )
    }

    @PostMapping("/code")
    fun sendEmailCode(@RequestBody request: SendEmailCodeWebRequest) {
        sendEmailCodeUseCase.execute(
            SendEmailCodeRequest(request.email, request.type.toString())
        )
    }

    @GetMapping("/email")
    fun certifyEmail(@RequestBody request: CertifyEmailWebRequest) {
        certifyEmailUseCase.execute(
            CertifyEmailRequest(
                request.accountId,
                request.email
            )
        )
    }
}
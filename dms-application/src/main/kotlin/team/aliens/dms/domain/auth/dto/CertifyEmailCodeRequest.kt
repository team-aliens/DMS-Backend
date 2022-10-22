package team.aliens.dms.domain.auth.dto

import team.aliens.dms.domain.auth.model.EmailType

data class CertifyEmailCodeRequest(
    val email: String,
    val authCode: String,
    val type: EmailType
) {
    constructor(email: String, authCode: String, type: String) : this(
        email = email,
        authCode = authCode,
        type = EmailType.valueOf(type)
    )
}
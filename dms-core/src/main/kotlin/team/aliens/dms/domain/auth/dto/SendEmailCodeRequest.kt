package team.aliens.dms.domain.auth.dto

import team.aliens.dms.domain.auth.model.EmailType

data class SendEmailCodeRequest(
    val email: String,
    val type: EmailType
) {
    constructor(email: String, type: String) : this(
        email = email,
        type = EmailType.valueOf(type)
    )
}

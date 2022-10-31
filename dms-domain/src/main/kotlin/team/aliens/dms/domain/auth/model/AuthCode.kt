package team.aliens.dms.domain.auth.model

import team.aliens.dms.common.annotation.Aggregate

@Aggregate
data class AuthCode(

    val code: String,

    val email: String,

    val type: EmailType,

    val expirationTime: Int

) {
    constructor(code: String, email: String, type: EmailType) : this(
        code = code,
        email = email,
        type = type,
        expirationTime = EXPIRED
    )

    companion object {
        const val EXPIRED = 180
    }
}

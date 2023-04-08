package team.aliens.dms.domain.auth.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException

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
        const val AUTH_CODE_SIZE = 6
    }

    fun validateAuthCode(code: String) {
        if (this.code != code) {
            throw AuthCodeMismatchException
        }
    }
}

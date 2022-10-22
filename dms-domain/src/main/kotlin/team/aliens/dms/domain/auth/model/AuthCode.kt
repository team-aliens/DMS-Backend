package team.aliens.dms.domain.auth.model

import team.aliens.dms.global.annotation.Aggregate
import java.util.*

@Aggregate
data class AuthCode(

    val code: String,

    val userId: UUID,

    val type: EmailType,

    val expirationTime: Int,
) {
    constructor(code: String, userId: UUID, type: EmailType) : this(
        code = code,
        userId = userId,
        type = type,
        expirationTime = EXPIRED
    )

    companion object {
        const val EXPIRED = 180
    }
}

package team.aliens.dms.domain.auth.model

import team.aliens.dms.domain.auth.exception.AuthCodeOverLimitException
import team.aliens.dms.global.annotation.Aggregate
import java.util.*

@Aggregate
data class AuthCodeLimit(

    val id: UUID,

    val email: String,

    val type: EmailType,

    val attemptCount: Int,

    val isVerified: Boolean,

    val expirationTime: Int
) {
    constructor(email: String, type: EmailType) : this(
        id = UUID.randomUUID(),
        email = email,
        type = type,
        attemptCount = 0,
        isVerified = false,
        expirationTime = EXPIRED
    )

    companion object {
        const val MAX_ATTEMPT_COUNT: Int = 5
        const val EXPIRED = 1800
        const val VERIFIED_EXPIRED = 2700

        fun certified(email: String, type: EmailType): AuthCodeLimit {
            return AuthCodeLimit(
                id = UUID.randomUUID(),
                email = email,
                type = type,
                attemptCount = MAX_ATTEMPT_COUNT,
                isVerified = true,
                expirationTime = VERIFIED_EXPIRED
            )
        }
    }

    fun increaseCount(): AuthCodeLimit {
        if (attemptCount >= MAX_ATTEMPT_COUNT) {
            throw AuthCodeOverLimitException
        }

        return AuthCodeLimit(
            id = id,
            email = email,
            type = type,
            attemptCount = attemptCount.inc(),
            isVerified = false,
            expirationTime = EXPIRED
        )
    }
}

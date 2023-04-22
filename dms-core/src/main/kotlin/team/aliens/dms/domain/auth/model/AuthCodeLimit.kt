package team.aliens.dms.domain.auth.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.domain.auth.exception.AuthCodeOverLimitException
import team.aliens.dms.domain.auth.exception.EmailAlreadyCertifiedException
import java.util.UUID

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
        const val MAX_ATTEMPT_COUNT = 5
        const val EXPIRED = 1800
        private const val VERIFIED_EXPIRED = 2700
    }

    fun certified(): AuthCodeLimit {
        return this.copy(
            id = id,
            email = email,
            type = type,
            attemptCount = MAX_ATTEMPT_COUNT,
            isVerified = true,
            expirationTime = VERIFIED_EXPIRED
        )
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

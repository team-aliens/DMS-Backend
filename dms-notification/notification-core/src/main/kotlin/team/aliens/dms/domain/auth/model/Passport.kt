package team.aliens.dms.domain.auth.model

import java.util.UUID

data class Passport(
    val user: PassportUser,
    val userIntegrity: String
)

data class PassportUser(
    val id: UUID,
    val schoolId: UUID,
    val authority: Authority
)

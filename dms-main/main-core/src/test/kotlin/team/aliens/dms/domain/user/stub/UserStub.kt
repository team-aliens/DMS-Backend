package team.aliens.dms.domain.user.stub

import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

internal fun createUserStub(
    id: UUID = UUID.randomUUID(),
    schoolId: UUID = UUID.randomUUID(),
    accountId: String = "accountId",
    password: String = "password",
    email: String = "email@dsm.hs.kr",
    authority: Authority = Authority.STUDENT,
    createdAt: LocalDateTime = LocalDateTime.now(),
    deletedAt: LocalDateTime? = null
) = User(
    id = id,
    schoolId = schoolId,
    accountId = accountId,
    password = password,
    email = email,
    authority = authority,
    createdAt = createdAt,
    deletedAt = deletedAt
)

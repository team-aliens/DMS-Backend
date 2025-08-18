package team.aliens.dms.domain.user.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import team.aliens.dms.domain.auth.model.Authority
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class User(

    val id: UUID = UUID(0, 0),

    override val schoolId: UUID,

    val accountId: String,

    val password: String,

    val email: String,

    val authority: Authority,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val deletedAt: LocalDateTime? = null

) : SchoolIdDomain

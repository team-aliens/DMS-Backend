package team.aliens.dms.domain.auth.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class RefreshToken(

    val token: String,

    val userId: UUID,

    val authority: Authority,

    val expirationTime: Int

)

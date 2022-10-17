package team.aliens.dms.domain.auth.model

import team.aliens.dms.global.annotation.Aggregate
import java.util.*

@Aggregate
data class RefreshToken(

    val token: String,

    val userId: UUID,

    val expirationTime: Int
)

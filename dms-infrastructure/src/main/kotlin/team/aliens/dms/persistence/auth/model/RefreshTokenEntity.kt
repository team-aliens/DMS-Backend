package team.aliens.dms.persistence.auth.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.*
import javax.validation.constraints.NotNull

@RedisHash("tbl_refresh_token")
data class RefreshTokenEntity(
    
    @Id
    val token: String,

    @field:NotNull
    val userId: UUID,

    @field:NotNull
    @TimeToLive
    val expirationTime: Int
)

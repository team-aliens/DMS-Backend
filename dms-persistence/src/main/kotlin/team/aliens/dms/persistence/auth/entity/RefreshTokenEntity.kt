package team.aliens.dms.persistence.auth.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import team.aliens.dms.domain.auth.model.Authority
import java.util.UUID

@RedisHash("tbl_refresh_token")
data class RefreshTokenEntity(

    @Id
    val token: String,

    val userId: UUID,

    val authority: Authority,

    @TimeToLive
    val expirationTime: Int

)

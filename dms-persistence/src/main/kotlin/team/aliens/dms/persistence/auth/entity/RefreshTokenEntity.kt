package team.aliens.dms.persistence.auth.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed
import team.aliens.dms.domain.auth.model.Authority
import java.util.UUID

@RedisHash("tbl_refresh_token")
data class RefreshTokenEntity(

    @Id
    val userId: UUID,

    @Indexed
    val token: String,

    val authority: Authority,

    @TimeToLive
    val expirationTime: Int

)

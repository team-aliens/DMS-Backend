package team.aliens.dms.persistence.auth.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed
import team.aliens.dms.domain.auth.model.EmailType
import java.util.UUID

@RedisHash("tbl_authcode_limit")
class AuthCodeLimitEntity(

    @Id
    val id: UUID,

    @field:Indexed
    val email: String,

    @field:Indexed
    val type: EmailType,

    val attemptCount: Int,

    val isVerified: Boolean,

    @TimeToLive
    val expirationTime: Int

)

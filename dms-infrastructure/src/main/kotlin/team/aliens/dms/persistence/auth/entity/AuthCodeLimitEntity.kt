package team.aliens.dms.persistence.auth.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed
import team.aliens.dms.domain.auth.model.EmailType
import java.util.UUID
import javax.validation.constraints.NotNull

@RedisHash("tbl_authcode_limit")
class AuthCodeLimitEntity(

    @Id
    val id: UUID,

    @field:NotNull
    @field:Indexed
    val email: String,

    @field:NotNull
    @field:Indexed
    val type: EmailType,

    @field:NotNull
    val attemptCount: Int,

    @field:NotNull
    val isVerified: Boolean,

    @field:NotNull
    @TimeToLive
    val expirationTime: Int

)

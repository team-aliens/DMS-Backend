package team.aliens.dms.persistence.auth.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import team.aliens.dms.domain.auth.model.EmailType
import java.util.*
import javax.validation.constraints.NotNull

@RedisHash("tbl_authcode_limit")
data class AuthCodeLimitEntity(

    @Id
    val id: UUID,

    @field:NotNull
    val email: String,

    @field:NotNull
    val type: EmailType,

    @field:NotNull
    val attemptCount: Int,

    @field:NotNull
    val isVerified: Boolean,

    @field:NotNull
    @TimeToLive
    val expirationTime: Int
)
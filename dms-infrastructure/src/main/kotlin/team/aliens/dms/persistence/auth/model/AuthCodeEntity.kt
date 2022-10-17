package team.aliens.dms.persistence.auth.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import team.aliens.dms.domain.auth.model.EmailType
import java.util.*
import javax.validation.constraints.NotNull

@RedisHash("tbl_authcode")
data class AuthCodeEntity(
    @Id
    val code: String,

    @field:NotNull
    val userId: UUID,

    @field:NotNull
    val type: EmailType,

    @field:NotNull
    @TimeToLive
    val expirationTime: Int,
)
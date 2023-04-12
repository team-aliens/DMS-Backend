package team.aliens.dms.persistence.auth.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed
import team.aliens.dms.domain.auth.model.EmailType

@RedisHash("tbl_authcode")
data class AuthCodeEntity(

    @Id
    val code: String,

    @Indexed
    val email: String,

    @Indexed
    val type: EmailType,

    @TimeToLive
    val expirationTime: Int

)

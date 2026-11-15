package team.aliens.dms.persistence.auth

import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.RefreshToken
import team.aliens.dms.domain.auth.spi.RefreshTokenPort
import java.time.Duration
import java.util.UUID

/**
 * 리프레시 토큰은 `refresh_token:{userId}:{token}` = `{authority}` 로 저장한다(TTL = 리프레시 만료 시간).
 * 로그인마다 키가 하나씩 생겨 앱·웹이 서로 덮어쓰지 않고, 유저의 토큰 전체는 `refresh_token:{userId}:*` 로 찾는다.
 *
 * 재발급하면 쓴 토큰은 바로 지우고 새 토큰을 저장한다. 지운 토큰이 다시 오면 없는 토큰과 같다.
 */
@Component
class RefreshTokenPersistenceAdapter(
    private val redisTemplate: StringRedisTemplate,
) : RefreshTokenPort {

    override fun queryRefreshTokenByUserIdAndToken(userId: UUID, token: String): RefreshToken? {
        val key = tokenKey(userId, token)
        val authority = redisTemplate.opsForValue().get(key) ?: return null

        return RefreshToken(
            token = token,
            userId = userId,
            authority = Authority.valueOf(authority),
            expirationTime = redisTemplate.getExpire(key).toInt()
        )
    }

    override fun save(refreshToken: RefreshToken): RefreshToken {
        redisTemplate.opsForValue().set(
            tokenKey(refreshToken.userId, refreshToken.token),
            refreshToken.authority.name,
            Duration.ofSeconds(refreshToken.expirationTime.toLong())
        )
        return refreshToken
    }

    override fun rotateRefreshToken(currentToken: String, newRefreshToken: RefreshToken): RefreshToken? {
        if (!redisTemplate.delete(tokenKey(newRefreshToken.userId, currentToken))) {
            return null
        }
        return save(newRefreshToken)
    }
    // Key 값들을 조회한 다음 해당 key들을 전부 삭제
    override fun deleteAllRefreshTokensByUserId(userId: UUID) {
        val options = ScanOptions.scanOptions().match("$KEY_PREFIX$userId:*").count(SCAN_COUNT).build()
        // Cursor를 순회 가능한 형태로 바꾸기 위해 Sequence로 변환한 뒤 List로 수집
        val keys = redisTemplate.scan(options).use { cursor ->
            cursor.asSequence().toList()
        }

        if (keys.isNotEmpty()) {
            redisTemplate.delete(keys)
        }
    }

    private fun tokenKey(userId: UUID, token: String) = "$KEY_PREFIX$userId:$token"

    companion object {
        private const val KEY_PREFIX = "refresh_token:"
        private const val SCAN_COUNT = 1000L
    }
}

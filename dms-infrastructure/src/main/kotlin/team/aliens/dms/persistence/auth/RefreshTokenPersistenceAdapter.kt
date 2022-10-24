package team.aliens.dms.persistence.auth

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.spi.RefreshTokenPort
import team.aliens.dms.persistence.auth.mapper.RefreshTokenMapper
import team.aliens.dms.persistence.auth.repository.RefreshTokenRepository

@Component
class RefreshTokenPersistenceAdapter(
    private val refreshTokenMapper: RefreshTokenMapper,
    private val refreshTokenRepository: RefreshTokenRepository
) : RefreshTokenPort {

    override fun queryRefreshTokenByToken(id: String) = refreshTokenMapper.toDomain(
        refreshTokenRepository.findByIdOrNull(id)
    )
}
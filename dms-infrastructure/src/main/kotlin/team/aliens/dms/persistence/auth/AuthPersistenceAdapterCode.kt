package team.aliens.dms.persistence.auth

import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.spi.AuthPort
import team.aliens.dms.persistence.auth.mapper.AuthCodeMapper
import team.aliens.dms.persistence.auth.repository.AuthCodeEntityRepository
import java.util.*

@Component
class AuthPersistenceAdapter(
    private val authCodeMapper: AuthCodeMapper,
    private val authCodeRepository: AuthCodeEntityRepository
) : AuthPort {

    override fun queryAuthCodeByUserId(userId: UUID) = authCodeMapper.toDomain(
        authCodeRepository.findByUserId(userId)
    )
}
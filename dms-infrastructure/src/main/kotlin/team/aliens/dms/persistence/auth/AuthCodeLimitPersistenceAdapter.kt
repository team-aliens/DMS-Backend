package team.aliens.dms.persistence.auth

import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.spi.AuthCodeLimitPort
import team.aliens.dms.persistence.auth.mapper.AuthCodeLimitMapper
import team.aliens.dms.persistence.auth.repository.AuthCodeLimitRepository
import java.util.UUID

@Component
class AuthCodeLimitPersistenceAdapter(
    private val authCodeLimitMapper: AuthCodeLimitMapper,
    private val authCodeLimitRepository: AuthCodeLimitRepository
) : AuthCodeLimitPort {

    override fun queryAuthCodeLimitByUserIdAndType(userId: UUID, type: EmailType) =
        authCodeLimitMapper.toDomain(
            authCodeLimitRepository.findByUserIdAndType(userId, type)
        )

    override fun saveAuthCodeLimit(authCodeLimit: AuthCodeLimit) = authCodeLimitMapper.toDomain(
        authCodeLimitRepository.save(authCodeLimitMapper.toEntity(authCodeLimit))
    )!!
}
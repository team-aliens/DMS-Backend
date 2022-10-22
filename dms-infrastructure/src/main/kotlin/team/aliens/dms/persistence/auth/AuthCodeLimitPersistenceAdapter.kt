package team.aliens.dms.persistence.auth

import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.spi.AuthCodeLimitPort
import team.aliens.dms.persistence.auth.mapper.AuthCodeLimitMapper
import team.aliens.dms.persistence.auth.repository.AuthCodeLimitRepository

@Component
class AuthCodeLimitPersistenceAdapter(
    private val authCodeLimitMapper: AuthCodeLimitMapper,
    private val authCodeLimitRepository: AuthCodeLimitRepository
) : AuthCodeLimitPort {

    override fun queryAuthCodeLimitByEmailAndEmailType(email: String, type: EmailType) =
        authCodeLimitMapper.toDomain(
            authCodeLimitRepository.findByEmailAndType(email, type)
        )

    override fun saveAuthCodeLimit(authCodeLimit: AuthCodeLimit) = authCodeLimitMapper.toDomain(
        authCodeLimitRepository.save(
            authCodeLimitMapper.toEntity(authCodeLimit)
        )
    )!!
}
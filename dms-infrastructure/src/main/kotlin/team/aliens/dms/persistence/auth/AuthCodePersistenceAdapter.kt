package team.aliens.dms.persistence.auth

import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.spi.AuthCodePort
import team.aliens.dms.persistence.auth.mapper.AuthCodeMapper
import team.aliens.dms.persistence.auth.repository.AuthCodeEntityRepository

@Component
class AuthCodePersistenceAdapter(
    private val authCodeMapper: AuthCodeMapper,
    private val authCodeRepository: AuthCodeEntityRepository
) : AuthCodePort {

    override fun saveAuthCode(authCode: AuthCode) = authCodeMapper.toDomain(
        authCodeRepository.save(
            authCodeMapper.toEntity(authCode)
        )
    )!!

    override fun queryAuthCodeByEmailAndEmailType(email: String, type: EmailType) = authCodeMapper.toDomain(
        authCodeRepository.findByEmailAndType(email, type)
    )

    override fun queryAuthCodeByEmail(email: String) = authCodeMapper.toDomain(
        authCodeRepository.findByEmail(email)
    )
}
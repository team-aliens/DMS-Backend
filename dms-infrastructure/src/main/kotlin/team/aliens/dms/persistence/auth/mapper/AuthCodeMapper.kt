package team.aliens.dms.persistence.auth.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.auth.model.AuthCodeEntity

@Component
class AuthCodeMapper : GenericMapper<AuthCode, AuthCodeEntity> {

    override fun toDomain(entity: AuthCodeEntity?): AuthCode? {
        return entity?.let {
            AuthCode(
                code = it.code,
                email = it.email,
                type = it.type,
                expirationTime = it.expirationTime,
            )
        }
    }

    override fun toEntity(domain: AuthCode): AuthCodeEntity {
        return AuthCodeEntity(
            code = domain.code,
            email = domain.email,
            type = domain.type,
            expirationTime = domain.expirationTime,
        )
    }
}
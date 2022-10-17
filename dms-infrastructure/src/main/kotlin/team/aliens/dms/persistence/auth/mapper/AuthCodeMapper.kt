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
                userId = it.userId,
                type = it.type,
                expirationTime = it.expirationTime,
            )
        }
    }

    override fun toEntity(domain: AuthCode): AuthCodeEntity {
        return AuthCodeEntity(
            code = domain.code,
            userId = domain.userId,
            type = domain.type,
            expirationTime = domain.expirationTime,
        )
    }

}
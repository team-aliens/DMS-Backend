package team.aliens.dms.persistence.auth.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.auth.model.AuthCodeLimitEntity

@Component
class AuthCodeLimitMapper : GenericMapper<AuthCodeLimit, AuthCodeLimitEntity> {

    override fun toDomain(entity: AuthCodeLimitEntity?): AuthCodeLimit? {
        return entity?.let {
            AuthCodeLimit(
                id = it.id,
                userId = it.userId,
                type = it.type,
                attemptCount = it.attemptCount,
                isVerified = it.isVerified,
                expirationTime = it.expirationTime
            )
        }
    }

    override fun toEntity(domain: AuthCodeLimit): AuthCodeLimitEntity {
        return AuthCodeLimitEntity(
            id = domain.id,
            userId = domain.userId,
            type = domain.type,
            attemptCount = domain.attemptCount,
            isVerified = domain.isVerified,
            expirationTime = domain.expirationTime
        )
    }

}
package team.aliens.dms.persistence.auth.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.RefreshToken
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.auth.model.RefreshTokenEntity

@Component
class RefreshTokenMapper : GenericMapper<RefreshToken, RefreshTokenEntity> {

    override fun toDomain(entity: RefreshTokenEntity?): RefreshToken? {
        return entity?.let {
            RefreshToken(
                token = it.token,
                userId = it.userId,
                expirationTime = it.expirationTime
            )
        }
    }

    override fun toEntity(domain: RefreshToken): RefreshTokenEntity {
        return RefreshTokenEntity(
            token = domain.token,
            userId = domain.userId,
            expirationTime = domain.expirationTime
        )
    }

}
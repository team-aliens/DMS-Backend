package team.aliens.dms.persistence.auth.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.auth.entity.RefreshTokenEntity

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshTokenEntity, String> {
    fun findByToken(token: String): RefreshTokenEntity?
}

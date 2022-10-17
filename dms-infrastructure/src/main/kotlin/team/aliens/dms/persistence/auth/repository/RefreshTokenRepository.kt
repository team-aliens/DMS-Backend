package team.aliens.dms.persistence.auth.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.auth.model.RefreshTokenEntity

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshTokenEntity, String> {
}
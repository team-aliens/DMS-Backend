package team.aliens.dms.persistence.auth.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.auth.model.AuthCodeEntity
import java.util.UUID

@Repository
interface AuthCodeEntityRepository : CrudRepository<AuthCodeEntity, String> {
    fun findByUserId(userId: UUID): AuthCodeEntity?
}
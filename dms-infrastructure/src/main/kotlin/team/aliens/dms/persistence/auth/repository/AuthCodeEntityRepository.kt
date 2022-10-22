package team.aliens.dms.persistence.auth.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.persistence.auth.model.AuthCodeEntity
import java.util.*

@Repository
interface AuthCodeEntityRepository : CrudRepository<AuthCodeEntity, String> {
    fun findByUserIdAndType(userId: UUID, type: EmailType) : AuthCodeEntity?
}
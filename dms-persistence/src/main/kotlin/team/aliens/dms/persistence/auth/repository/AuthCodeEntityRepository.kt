package team.aliens.dms.persistence.auth.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.persistence.auth.entity.AuthCodeEntity

@Repository
interface AuthCodeEntityRepository : CrudRepository<AuthCodeEntity, String> {

    fun findByEmail(email: String): AuthCodeEntity?

    fun findByEmailAndType(email: String, type: EmailType): AuthCodeEntity?
}

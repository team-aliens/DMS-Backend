package team.aliens.dms.persistence.auth.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.persistence.auth.model.AuthCodeLimitEntity
import java.util.*

@Repository
interface AuthCodeLimitRepository : CrudRepository<AuthCodeLimitEntity, UUID> {
    fun findByEmailAndType(email: String, type: EmailType): AuthCodeLimitEntity?
}
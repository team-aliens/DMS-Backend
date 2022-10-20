package team.aliens.dms.persistence.user.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.user.entity.UserJpaEntity
import java.util.UUID

@Repository
interface UserJpaRepository : CrudRepository<UserJpaEntity, UUID> {
    fun existsByEmail(email: String): Boolean
    fun existsByAccountId(accountId: String): Boolean
    fun findBySchoolId(schoolId: UUID) : UserJpaEntity?
    fun findByEmail(email: String) : UserJpaEntity?
    fun findByAccountId(accountId: String) : UserJpaEntity?
}
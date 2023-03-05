package team.aliens.dms.persistence.user.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.persistence.user.entity.UserJpaEntity
import java.util.UUID
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

@Repository
interface UserJpaRepository : CrudRepository<UserJpaEntity, UUID> {

    fun existsByEmail(email: String): Boolean

    @Query("select * from tbl_user as u where u.account_id=:accountId", nativeQuery = true)
    fun findExistsByAccountId(@Param("accountId") accountId: String): UserJpaEntity?

    fun findBySchoolIdAndAuthority(schoolId: UUID, authority: Authority): UserJpaEntity?

    fun findByEmail(email: String): UserJpaEntity?

    fun findByAccountId(accountId: String): UserJpaEntity?
}

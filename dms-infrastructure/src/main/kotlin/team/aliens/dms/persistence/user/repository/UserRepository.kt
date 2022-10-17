package team.aliens.dms.persistence.user.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.user.entity.UserJpaEntity
import java.util.UUID

@Repository
interface UserRepository : CrudRepository<UserJpaEntity, UUID> {
}
package team.aliens.dms.persistence.user.entity.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.user.entity.UserEntity
import java.util.UUID

@Repository
interface UserRepository : CrudRepository<UserEntity, UUID> {
}
package team.aliens.dms.persistence.manager.entity.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.manager.entity.ManagerEntity
import java.util.UUID

@Repository
interface ManagerRepository : CrudRepository<ManagerEntity, UUID> {
}
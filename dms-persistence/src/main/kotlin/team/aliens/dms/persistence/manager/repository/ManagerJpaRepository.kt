package team.aliens.dms.persistence.manager.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.manager.entity.ManagerJpaEntity
import java.util.UUID

@Repository
interface ManagerJpaRepository : CrudRepository<ManagerJpaEntity, UUID>

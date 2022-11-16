package team.aliens.dms.persistence.feature.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.feature.entity.AvailableServiceJpaEntity
import java.util.UUID

@Repository
interface AvailableServiceJpaRepository : CrudRepository<AvailableServiceJpaEntity, UUID> {
}
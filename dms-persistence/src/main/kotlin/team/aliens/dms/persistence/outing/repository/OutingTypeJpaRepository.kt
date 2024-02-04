package team.aliens.dms.persistence.outing.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.outing.entity.OutingTypeJpaEntity
import team.aliens.dms.persistence.outing.entity.OutingTypeJpaEntityId
import java.util.UUID

@Repository
interface OutingTypeJpaRepository : CrudRepository<OutingTypeJpaEntity, UUID> {

    fun findOutingTypeJpaEntityById(id: OutingTypeJpaEntityId) : OutingTypeJpaEntity

}

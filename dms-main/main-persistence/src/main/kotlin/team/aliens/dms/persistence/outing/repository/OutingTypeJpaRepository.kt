package team.aliens.dms.persistence.outing.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.outing.entity.OutingTypeJpaEntity
import team.aliens.dms.persistence.outing.entity.OutingTypeJpaEntityId

@Repository
interface OutingTypeJpaRepository : CrudRepository<OutingTypeJpaEntity, OutingTypeJpaEntityId>

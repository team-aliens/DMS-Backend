package team.aliens.dms.persistence.outing.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.outing.entity.OutingCompanionJpaEntity
import team.aliens.dms.persistence.outing.entity.OutingCompanionJpaEntityId

@Repository
interface OutingCompanionJpaRepository : CrudRepository<OutingCompanionJpaEntity, OutingCompanionJpaEntityId>{
}

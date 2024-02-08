package team.aliens.dms.persistence.outing.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.outing.entity.CompanionForOutingJpaEntity
import team.aliens.dms.persistence.outing.entity.CompanionForOutingJpaEntityId

@Repository
interface CompanionForOutingJpaRepository : CrudRepository<CompanionForOutingJpaEntity, CompanionForOutingJpaEntityId>{
}

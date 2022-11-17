package team.aliens.dms.persistence.point.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.point.entity.PhraseJpaEntity
import java.util.UUID

@Repository
interface PhraseJpaRepository : CrudRepository<PhraseJpaEntity, UUID> {
}
package team.aliens.dms.persistence.daybreak.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.daybreak.entity.DaybreakStudyApplicationJpaEntity
import java.util.UUID

@Repository
interface DaybreakStudyApplicationJpaRepository : CrudRepository<DaybreakStudyApplicationJpaEntity, UUID>

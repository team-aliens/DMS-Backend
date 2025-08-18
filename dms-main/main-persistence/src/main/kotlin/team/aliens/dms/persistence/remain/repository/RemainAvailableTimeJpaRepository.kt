package team.aliens.dms.persistence.remain.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.remain.entity.RemainAvailableTimeJpaEntity
import java.util.UUID

@Repository
interface RemainAvailableTimeJpaRepository : CrudRepository<RemainAvailableTimeJpaEntity, UUID>

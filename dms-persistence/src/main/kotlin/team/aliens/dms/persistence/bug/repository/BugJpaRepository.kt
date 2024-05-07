package team.aliens.dms.persistence.bug.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.bug.entity.BugReportJpaEntity
import java.util.UUID

@Repository
interface BugJpaRepository : CrudRepository<BugReportJpaEntity, UUID>

package team.aliens.dms.persistence.studyroom.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.AvailableTimeJpaEntity
import java.util.UUID

@Repository
interface AvailableTimeJpaRepository : CrudRepository<AvailableTimeJpaEntity, UUID>

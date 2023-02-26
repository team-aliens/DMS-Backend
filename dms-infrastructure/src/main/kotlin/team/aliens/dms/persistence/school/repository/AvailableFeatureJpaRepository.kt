package team.aliens.dms.persistence.school.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.school.entity.AvailableFeatureJpaEntity
import java.util.UUID

@Repository
interface AvailableFeatureJpaRepository : CrudRepository<AvailableFeatureJpaEntity, UUID>

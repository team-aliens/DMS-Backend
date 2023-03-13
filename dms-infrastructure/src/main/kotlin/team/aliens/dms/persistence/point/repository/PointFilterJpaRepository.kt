package team.aliens.dms.persistence.point.repository

import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.point.entity.PointFilterJpaEntity
import java.util.UUID

interface PointFilterJpaRepository : CrudRepository<PointFilterJpaEntity, UUID>{
}
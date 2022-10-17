package team.aliens.dms.persistence.point.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.point.entity.PointHistoryJpaEntity
import java.util.UUID

@Repository
interface PointHistoryRepository : CrudRepository<PointHistoryJpaEntity, UUID> {
}
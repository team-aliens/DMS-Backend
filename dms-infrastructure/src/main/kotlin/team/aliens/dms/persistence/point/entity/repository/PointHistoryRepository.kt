package team.aliens.dms.persistence.point.entity.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.point.entity.PointHistoryEntity
import java.util.UUID

@Repository
interface PointHistoryRepository : CrudRepository<PointHistoryEntity, UUID> {
}
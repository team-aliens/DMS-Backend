package team.aliens.dms.persistence.point.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.point.entity.PointHistoryJpaEntity
import java.util.UUID

@Repository
interface PointHistoryJpaRepository : JpaRepository<PointHistoryJpaEntity, UUID> {
    fun findByStudentGcnIn(gcns: List<String>): List<PointHistoryJpaEntity>
}

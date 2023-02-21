package team.aliens.dms.persistence.point.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.point.entity.PointOptionJpaEntity
import java.util.UUID

@Repository
interface PointOptionJpaRepository : CrudRepository<PointOptionJpaEntity, UUID> {
    fun findByIdAndSchoolId(id: UUID, schoolId: UUID): PointOptionJpaEntity?
}
package team.aliens.dms.persistence.remain.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.remain.entity.RemainStatusJpaEntity
import java.util.UUID

@Repository
interface RemainStatusJpaRepository : CrudRepository<RemainStatusJpaEntity, UUID> {
    fun deleteByRemainOptionId(remainOptionId: UUID)
    fun deleteByStudentId(studentId: UUID)
}
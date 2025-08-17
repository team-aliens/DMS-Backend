package team.aliens.dms.persistence.outing.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.outing.entity.OutingApplicationJpaEntity
import team.aliens.dms.persistence.outing.entity.OutingCompanionJpaEntity
import team.aliens.dms.persistence.outing.entity.OutingCompanionJpaEntityId
import java.util.UUID

@Repository
interface OutingCompanionJpaRepository : CrudRepository<OutingCompanionJpaEntity, OutingCompanionJpaEntityId> {
    fun deleteAllByOutingApplication(outingApplication: OutingApplicationJpaEntity)

    fun existsByStudentId(studentId: UUID): Boolean
}

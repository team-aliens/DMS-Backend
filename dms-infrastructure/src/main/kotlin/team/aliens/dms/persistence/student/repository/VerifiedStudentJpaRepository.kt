package team.aliens.dms.persistence.student.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.student.entity.VerifiedStudentJpaEntity
import team.aliens.dms.persistence.student.entity.VerifiedStudentJpaEntityId

@Repository
interface VerifiedStudentJpaRepository : CrudRepository<VerifiedStudentJpaEntity, VerifiedStudentJpaEntityId> {
}
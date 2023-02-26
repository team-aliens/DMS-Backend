package team.aliens.dms.persistence.student.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.student.entity.VerifiedStudentJpaEntity
import java.util.UUID

@Repository
interface VerifiedStudentJpaRepository : CrudRepository<VerifiedStudentJpaEntity, UUID> {

    fun findByGcnAndSchoolName(gcn: String, schoolName: String): VerifiedStudentJpaEntity?
}

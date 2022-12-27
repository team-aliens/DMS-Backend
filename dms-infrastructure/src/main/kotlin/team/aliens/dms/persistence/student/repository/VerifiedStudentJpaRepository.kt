package team.aliens.dms.persistence.student.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.student.entity.VerifiedStudentJpaEntity

@Repository
interface VerifiedStudentJpaRepository : CrudRepository<VerifiedStudentJpaEntity, UUID> {

    fun findByGcnAndSchoolName(gcn: String, schoolName: String): VerifiedStudentJpaEntity?

}
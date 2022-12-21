package team.aliens.dms.persistence.student.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.student.entity.VerifiedStudentEntity

@Repository
interface VerifiedStudentRepository : CrudRepository<VerifiedStudentEntity, UUID> {

    fun findByGcnAndSchoolName(gcn: String, schoolName: String): VerifiedStudentEntity?

}
package team.aliens.dms.persistence.student.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import java.util.UUID

@Repository
interface StudentJpaRepository : CrudRepository<StudentJpaEntity, UUID> {

    fun findByUserSchoolIdAndGradeAndClassRoomAndNumber(
        schoolId: UUID,
        grade: Int,
        classRoom: Int,
        number: Int
    ): StudentJpaEntity?

}
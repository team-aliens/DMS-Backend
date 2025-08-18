package team.aliens.dms.persistence.tag.repository

import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.tag.entity.StudentTagId
import team.aliens.dms.persistence.tag.entity.StudentTagJpaEntity
import java.util.UUID

interface StudentTagJpaRepository : CrudRepository<StudentTagJpaEntity, StudentTagId> {
    fun deleteAllByStudentIn(studentIds: List<UUID>)

    fun deleteByTagId(tagId: UUID)

    fun findAllByStudentId(studentId: UUID): List<StudentTagJpaEntity>
}

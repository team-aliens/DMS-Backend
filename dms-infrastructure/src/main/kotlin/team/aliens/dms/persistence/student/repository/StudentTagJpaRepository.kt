package team.aliens.dms.persistence.student.repository

import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.student.entity.StudentTagJpaEntity
import java.util.UUID

interface StudentTagJpaRepository : CrudRepository<StudentTagJpaEntity, UUID>

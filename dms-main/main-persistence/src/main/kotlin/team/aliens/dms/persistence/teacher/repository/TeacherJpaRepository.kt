package team.aliens.dms.persistence.teacher.repository

import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.teacher.entity.TeacherJpaEntity
import java.util.UUID

interface TeacherJpaRepository : CrudRepository<TeacherJpaEntity, UUID>

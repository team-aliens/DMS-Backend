package team.aliens.dms.persistence.student.repository

import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.student.entity.StudentTagJpaEntity

interface StudentTagJpaRepository : CrudRepository<StudentTagJpaEntity, StudentTagJpaEntity.Id>

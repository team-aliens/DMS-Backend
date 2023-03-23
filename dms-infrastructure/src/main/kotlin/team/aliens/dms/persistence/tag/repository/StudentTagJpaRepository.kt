package team.aliens.dms.persistence.tag.repository

import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.tag.entity.StudentTagId
import team.aliens.dms.persistence.tag.entity.StudentTagJpaEntity

interface StudentTagJpaRepository : CrudRepository<StudentTagJpaEntity, StudentTagId>

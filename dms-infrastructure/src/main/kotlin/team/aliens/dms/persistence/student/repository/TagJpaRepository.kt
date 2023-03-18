package team.aliens.dms.persistence.student.repository

import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.student.entity.TagJpaEntity
import java.util.UUID

interface TagJpaRepository : CrudRepository<TagJpaEntity, UUID>

package team.aliens.dms.persistence.tag.repository

import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.tag.entity.TagJpaEntity
import java.util.UUID

interface TagJpaRepository : CrudRepository<TagJpaEntity, UUID> {
    fun findBySchoolId(schoolId: UUID): List<TagJpaEntity>

    fun findByName(name: String): TagJpaEntity?

    fun existsByNameAndSchoolId(name: String, schoolId: UUID): Boolean
}

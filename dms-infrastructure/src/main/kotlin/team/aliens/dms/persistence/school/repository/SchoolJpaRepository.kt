package team.aliens.dms.persistence.school.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.util.*

@Repository
interface SchoolJpaRepository : CrudRepository<SchoolJpaEntity, UUID> {

    fun findByCode(code: String): SchoolJpaEntity?

}
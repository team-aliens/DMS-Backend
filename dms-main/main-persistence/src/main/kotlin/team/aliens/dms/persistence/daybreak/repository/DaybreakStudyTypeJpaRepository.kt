package team.aliens.dms.persistence.daybreak.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.daybreak.entity.DaybreakStudyTypeJpaEntity
import java.util.UUID

@Repository
interface DaybreakStudyTypeJpaRepository : CrudRepository<DaybreakStudyTypeJpaEntity, UUID> {

    fun findAllBySchoolJpaEntityId(schoolId: UUID): List<DaybreakStudyTypeJpaEntity>
}

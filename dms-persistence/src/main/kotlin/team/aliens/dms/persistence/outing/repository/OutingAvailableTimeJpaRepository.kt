package team.aliens.dms.persistence.outing.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.outing.entity.OutingAvailableTimeJpaEntity
import java.time.DayOfWeek
import java.util.UUID

@Repository
interface OutingAvailableTimeJpaRepository : CrudRepository<OutingAvailableTimeJpaEntity, UUID> {

    fun findByDayOfWeekAndSchoolId(dayOfWeek: DayOfWeek, school: UUID): OutingAvailableTimeJpaEntity?
}

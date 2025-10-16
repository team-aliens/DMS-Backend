package team.aliens.dms.persistence.studyroom.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.TimeSlotJpaEntity
import java.time.LocalTime
import java.util.UUID

@Repository
interface TimeSlotJpaRepository : CrudRepository<TimeSlotJpaEntity, UUID> {

    fun findBySchoolId(schoolId: UUID): List<TimeSlotJpaEntity>

    fun existsByStartTimeAndEndTimeAndSchoolId(startTime: LocalTime, endTime: LocalTime, schoolId: UUID): Boolean
}

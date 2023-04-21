package team.aliens.dms.persistence.studyroom.repository

import java.time.LocalTime
import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.TimeSlotJpaEntity

@Repository
interface TimeSlotJpaRepository : CrudRepository<TimeSlotJpaEntity, UUID> {

    fun findBySchoolId(schoolId: UUID): List<TimeSlotJpaEntity>

    fun existsByStartTimeAndEndTimeAndSchoolId(startTime: LocalTime, endTime: LocalTime, schoolId: UUID): Boolean
}

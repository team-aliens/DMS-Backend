package team.aliens.dms.persistence.studyroom.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.StudyRoomTimeSlotJpaEntity
import java.util.UUID

@Repository
interface StudyRoomTimeSlotJpaRepository : CrudRepository<StudyRoomTimeSlotJpaEntity, UUID> {

    fun findBySchoolId(schoolId: UUID): List<StudyRoomTimeSlotJpaEntity>

    fun existsBySchoolId(schoolId: UUID): Boolean
}

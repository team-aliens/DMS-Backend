package team.aliens.dms.persistence.studyroom.repository

import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.studyroom.entity.StudyRoomTimeSlotJpaEntity
import team.aliens.dms.persistence.studyroom.entity.StudyRoomTimeSlotJpaEntityId
import java.util.UUID

interface StudyRoomTimeSlotJpaRepository : CrudRepository<StudyRoomTimeSlotJpaEntity, StudyRoomTimeSlotJpaEntityId> {

    fun existsByTimeSlotId(timeSlotId: UUID): Boolean
}

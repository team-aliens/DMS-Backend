package team.aliens.dms.persistence.studyroom.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.studyroom.entity.StudyRoomTimeSlotJpaEntity
import team.aliens.dms.persistence.studyroom.entity.StudyRoomTimeSlotJpaEntityId

interface StudyRoomTimeSlotJpaRepository : CrudRepository<StudyRoomTimeSlotJpaEntity, StudyRoomTimeSlotJpaEntityId> {

    fun existsByTimeSlotId(timeSlotId: UUID): Boolean

    fun deleteByStudyRoomId(studyRoomId: UUID)
}

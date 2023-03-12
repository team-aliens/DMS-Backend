package team.aliens.dms.persistence.studyroom.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.StudyRoomJpaEntity
import java.util.UUID

@Repository
interface StudyRoomJpaRepository : CrudRepository<StudyRoomJpaEntity, UUID> {

    fun findByStudyRoomInfoId(studyRoomInfoId: UUID): List<StudyRoomJpaEntity>

    fun findByTimeSlotId(timeSlotId: UUID): List<StudyRoomJpaEntity>

    fun deleteByIdIn(id: List<UUID>)

    fun deleteByStudyRoomInfoId(studyRoomInfoId: UUID)
}

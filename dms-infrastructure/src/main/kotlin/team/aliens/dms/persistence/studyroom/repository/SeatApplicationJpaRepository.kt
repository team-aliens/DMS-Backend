package team.aliens.dms.persistence.studyroom.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.SeatApplicationJpaEntity
import java.util.UUID

@Repository
interface SeatApplicationJpaRepository : CrudRepository<SeatApplicationJpaEntity, UUID> {

    fun findByStudyRoomInfoId(studyRoomInfoId: UUID): List<SeatApplicationJpaEntity>

    fun findByTimeSlotId(timeSlotId: UUID): List<SeatApplicationJpaEntity>

    fun deleteByIdIn(id: List<UUID>)

    fun deleteByStudyRoomInfoId(studyRoomInfoId: UUID)
}

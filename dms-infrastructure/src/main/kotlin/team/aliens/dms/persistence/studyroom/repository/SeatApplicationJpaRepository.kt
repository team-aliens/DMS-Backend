package team.aliens.dms.persistence.studyroom.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.SeatApplicationJpaEntity
import java.util.UUID

@Repository
interface SeatApplicationJpaRepository : CrudRepository<SeatApplicationJpaEntity, UUID> {

    fun queryByStudentId(studentId: UUID): List<SeatApplicationJpaEntity>

    fun deleteByStudentId(studyRoomId: UUID)

    fun deleteByTimeSlotId(timeSlotId: UUID)
}

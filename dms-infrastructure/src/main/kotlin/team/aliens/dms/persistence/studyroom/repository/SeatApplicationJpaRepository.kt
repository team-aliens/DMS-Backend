package team.aliens.dms.persistence.studyroom.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.SeatApplicationJpaEntity
import team.aliens.dms.persistence.studyroom.entity.SeatApplicationJpaEntityId
import java.util.UUID

@Repository
interface SeatApplicationJpaRepository : CrudRepository<SeatApplicationJpaEntity, SeatApplicationJpaEntityId> {

    fun queryByStudentId(studentId: UUID): List<SeatApplicationJpaEntity>

    fun findByTimeSlotId(timeSlotId: UUID): List<SeatApplicationJpaEntity>

    fun deleteByStudentId(studyRoomId: UUID)

    fun deleteByTimeSlotId(timeSlotId: UUID)

    fun deleteByStudentIdAndTimeSlotId(studentId: UUID, timeSlotId: UUID)
}

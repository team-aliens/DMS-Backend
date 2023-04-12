package team.aliens.dms.persistence.studyroom.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.SeatApplicationJpaEntity
import team.aliens.dms.persistence.studyroom.entity.SeatApplicationJpaEntityId

@Repository
interface SeatApplicationJpaRepository : CrudRepository<SeatApplicationJpaEntity, SeatApplicationJpaEntityId> {

    fun queryByStudentId(studentId: UUID): List<SeatApplicationJpaEntity>

    fun queryByStudentIdAndTimeSlotId(studentId: UUID, timeSlotId: UUID): SeatApplicationJpaEntity?

    fun deleteByStudentId(studyRoomId: UUID)

    fun deleteByTimeSlotId(timeSlotId: UUID)

    fun deleteBySeatIdAndStudentIdAndTimeSlotId(seatId: UUID, studentId: UUID, timeSlotId: UUID)

    fun deleteBySeatStudyRoomId(studyRoomId: UUID)
}

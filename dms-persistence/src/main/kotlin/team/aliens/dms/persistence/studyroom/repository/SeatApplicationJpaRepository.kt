package team.aliens.dms.persistence.studyroom.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.SeatApplicationJpaEntity
import team.aliens.dms.persistence.studyroom.entity.SeatApplicationJpaEntityId
import java.util.UUID

@Repository
interface SeatApplicationJpaRepository : CrudRepository<SeatApplicationJpaEntity, SeatApplicationJpaEntityId> {

    fun queryByStudentId(studentId: UUID): List<SeatApplicationJpaEntity>

    fun queryByStudentIdAndTimeSlotId(studentId: UUID, timeSlotId: UUID): SeatApplicationJpaEntity?

    fun deleteByStudentId(studyRoomId: UUID)

    fun deleteByTimeSlotId(timeSlotId: UUID)

    fun deleteByStudentIdAndSeatIdAndTimeSlotId(studentId: UUID, seatId: UUID, timeSlotId: UUID)

    fun deleteByStudentIdAndTimeSlotId(studentId: UUID, timeSlotId: UUID)

    fun deleteBySeatIdAndStudentIdAndTimeSlotId(seatId: UUID, studentId: UUID, timeSlotId: UUID)

    fun deleteBySeatStudyRoomId(studyRoomId: UUID)
}

package team.aliens.dms.persistence.studyroom.repository

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.SeatJpaEntity

@Repository
interface SeatJpaRepository : JpaRepository<SeatJpaEntity, UUID> {

    fun findByStudentId(studentId: UUID): SeatJpaEntity?

    fun deleteAllByStudyRoomId(studyRoomId: UUID)

    fun findByStudyRoomId(studyRoomId: UUID): SeatJpaEntity?

    fun existsByTypeId(seatTypeId: UUID): Boolean

}
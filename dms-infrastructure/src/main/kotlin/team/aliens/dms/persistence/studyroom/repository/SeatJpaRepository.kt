package team.aliens.dms.persistence.studyroom.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.SeatJpaEntity
import java.util.UUID

@Repository
interface SeatJpaRepository : JpaRepository<SeatJpaEntity, UUID> {

    fun existsByTypeId(seatTypeId: UUID): Boolean

    fun deleteByStudyRoomId(studyRoomId: UUID)
}

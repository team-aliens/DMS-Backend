package team.aliens.dms.persistence.studyroom.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.SeatTypeJpaEntity

@Repository
interface SeatTypeJpaRepository : CrudRepository<SeatTypeJpaEntity, UUID> {

    fun findBySchoolId(schoolId: UUID): List<SeatTypeJpaEntity>

}
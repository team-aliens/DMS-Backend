package team.aliens.dms.persistence.studyroom.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.SeatTypeJpaEntity
import java.util.UUID

@Repository
interface SeatTypeJpaRepository : CrudRepository<SeatTypeJpaEntity, UUID> {

    fun findAllBySchoolId(schoolId: UUID): List<SeatTypeJpaEntity>

    fun existsByNameAndSchoolId(name: String, schoolId: UUID): Boolean
}

package team.aliens.dms.persistence.room.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.room.entity.RoomJpaEntity
import java.util.UUID

@Repository
interface RoomJpaRepository : CrudRepository<RoomJpaEntity, UUID> {

    fun findBySchoolIdAndNumber(schoolId: UUID, number: String): RoomJpaEntity?

    fun findAllByNumberIsInAndSchoolId(numbers: List<String>, schoolId: UUID): List<RoomJpaEntity>
}

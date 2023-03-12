package team.aliens.dms.persistence.studyroom.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.StudyRoomInfoJpaEntity
import java.util.UUID

@Repository
interface StudyRoomInfoJpaRepository : CrudRepository<StudyRoomInfoJpaEntity, UUID> {

    fun existsByNameAndFloorAndSchoolId(name: String, floor: Int, schoolId: UUID): Boolean
}

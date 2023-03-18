package team.aliens.dms.persistence.studyroom.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.StudyRoomJpaEntity
import java.util.UUID

@Repository
interface StudyRoomJpaRepository : CrudRepository<StudyRoomJpaEntity, UUID> {

    fun findBySchoolId(schoolId: UUID): List<StudyRoomJpaEntity>

    fun existsByNameAndFloorAndSchoolId(name: String, floor: Int, schoolId: UUID): Boolean
}

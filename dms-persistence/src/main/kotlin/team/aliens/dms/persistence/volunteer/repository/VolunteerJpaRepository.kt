package team.aliens.dms.persistence.volunteer.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.volunteer.entity.VolunteerJpaEntity
import java.util.UUID

@Repository
interface VolunteerJpaRepository : CrudRepository<VolunteerJpaEntity, UUID> {

}

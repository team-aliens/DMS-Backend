package team.aliens.dms.persistence.volunteer.repository

import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.volunteer.entity.VolunteerScoreJpaEntity
import java.util.UUID

interface VolunteerScoreRepository : CrudRepository<VolunteerScoreJpaEntity, UUID>

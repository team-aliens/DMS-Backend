package team.aliens.dms.persistence.security.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.security.entity.SchoolSecretJpaEntity
import java.util.UUID

@Repository
interface SchoolSecretJpaRepository : CrudRepository<SchoolSecretJpaEntity, UUID>

package team.aliens.dms.persistence.vote.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.vote.entity.ExcludedStudentJpaEntity
import java.util.UUID

@Repository
interface ExcludedStudentJpaRepository : JpaRepository<ExcludedStudentJpaEntity, UUID>

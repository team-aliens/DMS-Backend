package team.aliens.dms.persistence.point.repository

import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.point.entity.PhraseJpaEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import team.aliens.dms.domain.point.model.PointType

@Repository
interface PhraseJpaRepository : JpaRepository<PhraseJpaEntity, UUID> {

    fun findAllByTypeAndStandard(type: PointType, standard: Int): List<PhraseJpaEntity>

}
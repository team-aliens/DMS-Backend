package team.aliens.dms.persistence.outing

import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.OutingCompanion
import team.aliens.dms.domain.outing.spi.OutingCompanionPort
import team.aliens.dms.persistence.outing.mapper.OutingCompanionMapper
import team.aliens.dms.persistence.outing.repository.OutingCompanionJpaRepository

@Component
class OutingCompanionsPersistenceAdapter(
    private val outingCompanionRepository: OutingCompanionJpaRepository,
    private val outingCompanionMapper: OutingCompanionMapper
) : OutingCompanionPort {

    override fun saveAllOutingCompanions(outingCompanions: List<OutingCompanion>) {
        outingCompanionRepository.saveAll(
            outingCompanions.map {
                outingCompanionMapper.toEntity(it)
            }
        )
    }
}
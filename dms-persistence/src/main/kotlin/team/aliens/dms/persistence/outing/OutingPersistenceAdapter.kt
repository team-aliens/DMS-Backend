package team.aliens.dms.persistence.outing

import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.spi.OutingApplicationPort
import team.aliens.dms.persistence.outing.mapper.OutingApplicationMapper
import team.aliens.dms.persistence.outing.repository.OutingApplicationJpaRepository

@Component
class OutingPersistenceAdapter(
    private val outingApplicationMapper: OutingApplicationMapper,
    private val outingApplicationRepository: OutingApplicationJpaRepository
) : OutingApplicationPort {
    override fun saveOutingApplication(outingApplication: OutingApplication): OutingApplication {
        return outingApplicationMapper.toDomain(
            outingApplicationRepository.save(outingApplicationMapper.toEntity(outingApplication))
        )!!
    }

}

package team.aliens.dms.persistence.manager

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.manager.spi.ManagerPort
import team.aliens.dms.persistence.manager.mapper.ManagerMapper
import team.aliens.dms.persistence.manager.repository.ManagerJpaRepository
import java.util.UUID

@Component
class ManagerPersistenceAdapter(
    private val managerRepository: ManagerJpaRepository,
    private val managerMapper: ManagerMapper
) : ManagerPort {

    override fun queryManagerById(managerId: UUID) = managerMapper.toDomain(
        managerRepository.findByIdOrNull(managerId)
    )
}
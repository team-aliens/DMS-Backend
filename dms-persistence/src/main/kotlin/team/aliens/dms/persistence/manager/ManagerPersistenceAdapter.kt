package team.aliens.dms.persistence.manager

import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.manager.spi.ManagerPort
import team.aliens.dms.persistence.manager.mapper.ManagerMapper
import team.aliens.dms.persistence.manager.repository.ManagerJpaRepository

@Component
class ManagerPersistenceAdapter(
    private val managerRepository: ManagerJpaRepository,
    private val managerMapper: ManagerMapper
) : ManagerPort {

    override fun queryManagerById(managerId: UUID) = managerMapper.toDomain(
        managerRepository.findByIdOrNull(managerId)
    )

    override fun existsManagerById(managerId: UUID) =
        managerRepository.existsById(managerId)
}

package team.aliens.dms.persistence.manager.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.manager.model.Manager
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.manager.entity.ManagerJpaEntity
import team.aliens.dms.persistence.user.repository.UserRepository

@Component
class ManagerMapper(
    private val userRepository: UserRepository
) : GenericMapper<Manager, ManagerJpaEntity> {

    override fun toDomain(e: ManagerJpaEntity): Manager {
        return Manager(
            managerId = e.managerId
        )
    }

    override fun toEntity(d: Manager): ManagerJpaEntity {
        val user = userRepository.findByIdOrNull(d.managerId) ?: throw UserNotFoundException

        return ManagerJpaEntity(
            managerId = d.managerId,
            userJpaEntity = user
        )
    }
}
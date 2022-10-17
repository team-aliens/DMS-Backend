package team.aliens.dms.persistence.manager.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.manager.model.Manager
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.manager.entity.ManagerJpaEntity
import team.aliens.dms.persistence.user.repository.UserJpaRepository

@Component
class ManagerMapper(
    private val userJpaRepository: UserJpaRepository
) : GenericMapper<Manager, ManagerJpaEntity> {

    override fun toDomain(entity: ManagerJpaEntity?): Manager? {
        return entity?.let {
            Manager(managerId = it.managerId)
        }
    }

    override fun toEntity(domain: Manager): ManagerJpaEntity {
        val user = userJpaRepository.findByIdOrNull(domain.managerId) ?: throw UserNotFoundException

        return ManagerJpaEntity(
            managerId = domain.managerId,
            user = user
        )
    }
}
package team.aliens.dms.persistence.manager.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.manager.model.Manager
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.manager.entity.ManagerEntity
import team.aliens.dms.persistence.user.repository.UserRepository

@Component
class ManagerMapper(
    private val userRepository: UserRepository
) : GenericMapper<Manager, ManagerEntity> {

    override fun toDomain(e: ManagerEntity): Manager {
        return Manager(
            managerId = e.managerId
        )
    }

    override fun toEntity(d: Manager): ManagerEntity {
        val user = userRepository.findByIdOrNull(d.managerId) ?: throw RuntimeException()

        return ManagerEntity(
            managerId = d.managerId,
            userEntity = user
        )
    }
}
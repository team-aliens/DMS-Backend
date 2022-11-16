package team.aliens.dms.persistence.manager.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.manager.model.Manager
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.manager.entity.ManagerJpaEntity
import team.aliens.dms.persistence.user.repository.UserJpaRepository
import java.util.UUID

@Component
class ManagerMapper(
    private val userJpaRepository: UserJpaRepository
) : GenericMapper<Manager, ManagerJpaEntity> {

    override fun toDomain(entity: ManagerJpaEntity?): Manager? {
        return entity?.let {
            Manager(
                managerId = it.userId,
                schoolId = it.user!!.school!!.id,
                name = it.name,
                profileImageUrl = it.profileImageUrl
            )
        }
    }

    override fun toEntity(domain: Manager): ManagerJpaEntity {
        val user = userJpaRepository.findByIdOrNull(domain.managerId)

        return ManagerJpaEntity(
            userId = domain.managerId,
            user = user,
            name = domain.name,
            profileImageUrl = domain.profileImageUrl!!
        )
    }
}
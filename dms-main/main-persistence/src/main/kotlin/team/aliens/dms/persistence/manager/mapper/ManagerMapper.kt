package team.aliens.dms.persistence.manager.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.manager.model.Manager
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.manager.entity.ManagerJpaEntity
import team.aliens.dms.persistence.user.repository.UserJpaRepository

@Component
class ManagerMapper(
    private val userRepository: UserJpaRepository
) : GenericMapper<Manager, ManagerJpaEntity> {

    override fun toDomain(entity: ManagerJpaEntity?): Manager? {
        return entity?.let {
            Manager(
                id = it.id,
                schoolId = it.user!!.school!!.id!!,
                name = it.name,
                profileImageUrl = it.profileImageUrl
            )
        }
    }

    override fun toEntity(domain: Manager): ManagerJpaEntity {
        val user = userRepository.findByIdOrNull(domain.id)

        return ManagerJpaEntity(
            id = domain.id,
            user = user,
            name = domain.name,
            profileImageUrl = domain.profileImageUrl!!
        )
    }
}
